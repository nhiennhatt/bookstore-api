package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import com.nhiennhatt.bookstoreapi.dto.orders.CreatePaymentIntentDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDetailDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDto;
import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.services.OrderService;
import com.nhiennhatt.bookstoreapi.validations.order.CreateOrderValidation;
import com.nhiennhatt.bookstoreapi.validations.order.OrderFilter;
import com.nhiennhatt.bookstoreapi.validations.order.PreviewOrderValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "The Orders API")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private Validator validator;

    @GetMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) UUID cursor,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String paymentCode,
            @RequestParam(required = false) String deliveryCode,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer provinceId,
            @RequestParam(required = false) String wardCode,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate
    ) {
        OrderFilter filter = OrderFilter.builder()
                .cursor(cursor)
                .status(status)
                .paymentCode(paymentCode)
                .deliveryCode(deliveryCode)
                .districtId(districtId)
                .provinceId(provinceId)
                .wardCode(wardCode)
                .startDate(startDate)
                .endDate(endDate)
                .limit(limit)
                .build();

        Set<ConstraintViolation<OrderFilter>> violations = validator.validate(filter);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        return ResponseEntity.ok(orderService.getOrders(filter));
    }


    @PostMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderValidation order, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.status(201).body(orderService.createOrder(order, user));
    }

    @PostMapping("/preview")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> previewOrder(@Valid @RequestBody PreviewOrderValidation order, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(orderService.previewOrder(order, user));
    }

    @PostMapping("/{id}/payment-intent")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreatePaymentIntentDto> createPaymentIntent(@PathVariable() UUID id, @AuthenticationPrincipal CurrentUser user) {
        String clientSecret = orderService.createPaymentIntent(id, user);
        return ResponseEntity.status(201).body(new CreatePaymentIntentDto(clientSecret));
    }

    @GetMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-auth")},
            description = "Only allow ROLE_CONTENT_MANAGER, ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_PACKER, and the order owner to access this resource."
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable() UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(orderService.getOrderById(id, user));
    }
}
