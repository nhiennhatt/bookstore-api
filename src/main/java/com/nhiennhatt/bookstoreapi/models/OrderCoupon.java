package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_coupons")
@Getter
@Setter
public class OrderCoupon extends Base {

    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "uuid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "coupon_id", columnDefinition = "uuid", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private String type;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

}
