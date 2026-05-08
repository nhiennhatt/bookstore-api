package com.nhiennhatt.bookstoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "details"})
public class Order extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", columnDefinition = "uuid", nullable = false)
    private UserAddress address;

    @Column(name = "address_id", insertable = false, updatable = false)
    private UUID addressId;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "delivery_code")
    private String deliveryCode;

    @Column(name = "payment_code")
    private String paymentCode;

    @Column(name = "total_weight", nullable = false)
    private int totalWeight;

    @Column(name = "payment_client_secret")
    private String paymentClientSecret;

    @Column(name = "subtotal_price", nullable = false)
    private int subtotalPrice;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "order_discount")
    private int orderDiscount;

    @Column(name = "shipping_discount")
    private int shippingDiscount;

    @Column(name = "grand_total", nullable = false)
    private int grandTotal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderDetail> details;
}
