package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coupons")
@Getter
@Setter
public class Coupon extends Base {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 18)
    private String code;

    @Column(name = "calculation_type", nullable = false)
    private String calculationType;

    @Column(nullable = false)
    private float value;

    @Column(name = "coupon_type", nullable = false)
    private String couponType;

    @Column(name = "max_discount_amount")
    private float maxDiscountAmount;

    @Column(name = "usage_limit_global")
    private int usageLimitGlobal;

    @Column(name = "usage_limit_user")
    private int usageLimitUser;

    @Column(nullable = false)
    private String status;

    @Column(name = "start_at")
    private long startAt;

    @Column(name = "end_at")
    private long endAt;

}
