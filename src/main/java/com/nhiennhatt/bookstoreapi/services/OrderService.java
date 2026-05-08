package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.*;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNCalShippingFeeBody;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNCalShippingFeeResponse;
import com.nhiennhatt.bookstoreapi.common.classes.GHN.GHNCreateOrderBody;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDetailDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDto;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.models.OrderDetail;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.repository.*;
import com.nhiennhatt.bookstoreapi.repository.projection.BookWithVariantForOrderProjection;
import com.nhiennhatt.bookstoreapi.utils.GHNUtil;
import com.nhiennhatt.bookstoreapi.utils.StripeUtils;
import com.nhiennhatt.bookstoreapi.validations.order.CreateOrderItemValidation;
import com.nhiennhatt.bookstoreapi.validations.order.CreateOrderValidation;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${stripe.wh-sct}")
    private String webhookSecret;
    @Value("${ghn.token}")
    private String ghnToken;
    @Value("${ghn.shop-id}")
    private int shopId;

    @Autowired
    private MinioService minioService;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookVariantRepository bookVariantRepository;
    private final BookRepository bookRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            BookVariantRepository bookVariantRepository,
            BookRepository bookRepository,
            AddressRepository addressRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.bookVariantRepository = bookVariantRepository;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public OrderDto previewOrder(CreateOrderValidation orderBody, CurrentUser user) {
        Order order = buildOrder(orderBody, user);
        return OrderDto.builder()
                .address(order.getAddress())
                .grandTotal(order.getGrandTotal())
                .shippingFee(order.getShippingFee())
                .subtotalPrice(order.getSubtotalPrice())
                .orderDetails(
                        order.getDetails().stream().map(d ->
                                OrderDetailDto.builder()
                                        .variantId(d.getBookVariant().getId())
                                        .bookId(d.getBookVariant().getBookId())
                                        .quantity(d.getQuantity())
                                        .unitPrice(d.getUnitPrice())
                                        .totalPrice(d.getTotalPrice())
                                        .originUnitPrice(d.getOriginUnitPrice())
                                        .variantName(d.getBookVariant().getName())
                                        .bookName(d.getBookVariant().getBook().getName())
                                        .build()
                        ).toList()
                )
                .build();
    }

    @Transactional
    public Order createOrder(CreateOrderValidation orderBody, CurrentUser user) {
        Order order = buildOrder(orderBody, user);

        orderRepository.save(order);
        orderDetailRepository.saveAll(order.getDetails());

        order.getDetails().forEach(v -> bookVariantRepository.addInventory(v.getId(), v.getQuantity() * -1));
        return order;
    }

    public String createPaymentIntent(UUID id, CurrentUser user) {
        Order order = orderRepository.findOrderById(id);
        if (order == null || !user.getId().equals(order.getUserId()) || order.getStatus() != OrderStatus.PAYING)
            throw new AppException("Order not found", "ORDER_NOT_FOUND", 404, null, null);
        if (order.getPaymentCode() != null)
            return order.getPaymentCode() + "_" + order.getPaymentClientSecret();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) order.getGrandTotal())
                .setCurrency("vnd")
                .putMetadata("order_id", order.getId().toString())
                .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String clientSecret = paymentIntent.getClientSecret();

            String piCode = StripeUtils.extractPiCode(clientSecret);
            String clientSecretCode = StripeUtils.extractClientSecret(clientSecret);
            order.setPaymentCode(piCode);
            order.setPaymentClientSecret(clientSecretCode);

            orderRepository.save(order);
            return clientSecret;
        } catch (StripeException e) {
            logger.error("Stripe error: {}", e.getMessage());
            throw new AppException("Stripe error", "STRIPE_ERROR", 500, null, e.getMessage());
        }
    }

    public void verifyPayment(String payload, String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Webhook signature verification failed: {}", e.getMessage());
            return;
        }

        if (event.getType().equals("payment_intent.succeeded")) {
            updateOrderStatus(event);
        }
    }

    private void updateOrderStatus(Event event) {
        StripeObject obj = event.getDataObjectDeserializer().getObject().orElse(null);
        if (!(obj instanceof PaymentIntent intent)) return;
        Map<String, String> metadata = intent.getMetadata();
        String orderId = metadata.get("order_id");

        Order order = orderRepository.findOrderById(UUID.fromString(orderId));
        if (order == null) return;
        order.setStatus(OrderStatus.SHIPPING);
        String shippingOrderCode = GHNUtil.createShippingOrder(
                ghnToken,
                GHNCreateOrderBody.builder()
                        .shopId(shopId)
                        .weight(order.getTotalWeight())
                        .toWardCode(order.getAddress().getWardCode())
                        .toDistrictId(order.getAddress().getProvinceId())
                        .toAddress(order.getAddress().getAddress())
                        .toPhone(order.getAddress().getPhone())
                        .toName(order.getAddress().getName())
                        .fromName("NhienNhat")
                        .requiredNote("CHOXEMHANGKHONGTHU")
                        .paymentTypeId(1)
                        .items(
                                order.getDetails().stream().map(d ->
                                        GHNCreateOrderBody.GHNOrderItem.builder()
                                                .name(d.getBookVariant().getName())
                                                .quantity(d.getQuantity())
                                                .build()
                                ).toList()
                        )
                        .serviceTypeId(2)
                        .build()
        );
        order.setDeliveryCode(shippingOrderCode);
        orderRepository.save(order);
    }

    private Order buildOrder(CreateOrderValidation orderBody, CurrentUser user) {
        List<CreateOrderItemValidation> variants = orderBody.getVariants();
        List<BookWithVariantForOrderProjection> variantDetail = bookVariantRepository.getBookWithVariantForOrder(
                variants.stream().map(CreateOrderItemValidation::getVariantId).toList()
        );
        UserAddress address = addressRepository.findUserAddressById(orderBody.getAddressId());
        if (address == null)
            throw new AppException("Address not found", "ADDRESS_NOT_FOUND", 404, null, null);

        Map<CreateOrderItemValidation, BookWithVariantForOrderProjection> mappedVariants = new HashMap<>();
        variants.forEach(v -> {
            Optional<BookWithVariantForOrderProjection> ref = variantDetail.stream().findFirst();
            if (ref.isPresent()) {
                BookWithVariantForOrderProjection book = ref.get();
                if (book.getBookStatus() == BookStatus.ACTIVE
                        && book.getVariantStatus() == BookVariantStatus.ACTIVE
                        && book.getInventory() >= v.getQuantity()
                ) {
                    mappedVariants.put(v, book);
                    return;
                }
                throw new AppException("Quantity is invalid", "QUANTITY_IS_INVALID", 400, null, "variantId: " + v.getVariantId() + ".");
            }
            throw new AppException("Book variant not found", "BOOK_VARIANT_NOT_FOUND", 404, null, "variantId: " + v.getVariantId() + ".");
        });

        int totalWeight = mappedVariants.entrySet().stream()
                .map(t -> t.getValue().getWeight() * t.getKey().getQuantity())
                .reduce(0, Integer::sum);

        int subtotalPrice = mappedVariants.entrySet().stream()
                .map(t -> t.getValue().getOriginPrice() * t.getKey().getQuantity())
                .reduce(0, Integer::sum);

        int grandPrice = mappedVariants.entrySet().stream()
                .map(t -> (t.getValue().getSalePrice() == 0 ? t.getValue().getOriginPrice() : t.getValue().getSalePrice()) * t.getKey().getQuantity())
                .reduce(0, Integer::sum);

        GHNCalShippingFeeResponse shippingFee = GHNUtil.calShippingFee(
                ghnToken,
                GHNCalShippingFeeBody.builder()
                        .shopId(shopId)
                        .weight(totalWeight)
                        .toWardCode(address.getWardCode())
                        .toDistrictId(address.getProvinceId())
                        .serviceTypeId(2)
                        .build()
        );

        grandPrice += shippingFee != null && shippingFee.getData() != null ? shippingFee.getData().getTotal() : 0;
        subtotalPrice += shippingFee != null && shippingFee.getData() != null ? shippingFee.getData().getTotal() : 0;

        Order order = new Order();
        order.setSubtotalPrice(subtotalPrice);
        order.setGrandTotal(grandPrice);
        order.setShippingFee(shippingFee != null && shippingFee.getData() != null ? shippingFee.getData().getTotal() : 0);
        order.setAddress(address);
        order.setUser(userRepository.getReferenceById(user.getId()));
        order.setStatus(OrderStatus.PAYING);
        order.setTotalWeight(totalWeight);


        List<OrderDetail> details = mappedVariants.entrySet().stream().map(v -> {
            OrderDetail detail = new OrderDetail();
            int originPrice = v.getValue().getOriginPrice();
            int salePrice = v.getValue().getSalePrice() == 0 ? originPrice : v.getValue().getSalePrice();
            int quantity = v.getKey().getQuantity();
            detail.setOrder(order);
            detail.setBookVariant(bookVariantRepository.getReferenceById(v.getKey().getVariantId()));
            detail.getBookVariant().setBookId(v.getValue().getBookId());
            detail.getBookVariant().setName(v.getValue().getVariantName());
            detail.getBookVariant().setBook(bookRepository.getReferenceById(v.getValue().getBookId()));
            detail.getBookVariant().getBook().setName(v.getValue().getBookName());
            detail.setQuantity(quantity);
            detail.setOriginUnitPrice(originPrice);
            detail.setUnitPrice(salePrice);
            detail.setTotalPrice(salePrice * quantity);
            return detail;
        }).toList();

        order.setDetails(details);

        return order;
    }

    public OrderDto getOrderById(UUID id, CurrentUser user) {
        Order order = orderRepository.findOrderByIdWithRelated(id);
        if (order == null || (user.getRole() == UserRole.ROLE_CUSTOMER && !user.getId().equals(order.getUserId())))
            throw new AppException("Order not found", "ORDER_NOT_FOUND", 404, null, null);
        List<OrderDetailDto> orderDetails = orderDetailRepository.findOrderDetailsByOrderId(id);
        orderDetails = orderDetails.stream().peek(o -> {
            try {
                o.setImage(minioService.getPresignedUrl(o.getImage()));
            } catch (Exception e) {
                o.setImage(null);
            }
        }).toList();
        return OrderDto.builder()
                .id(order.getId())
                .orderDiscount(order.getOrderDiscount())
                .shippingDiscount(order.getShippingDiscount())
                .subtotalPrice(order.getSubtotalPrice())
                .shippingFee(order.getShippingFee())
                .grandTotal(order.getGrandTotal())
                .status(order.getStatus())
                .user(new MeResponse(order.getUser()))
                .address(order.getAddress())
                .orderDetails(orderDetails)
                .build();
    }
}
