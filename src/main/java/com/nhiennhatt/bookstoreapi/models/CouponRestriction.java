package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coupon_restrictions")
@Getter
@Setter
public class CouponRestriction extends Base {

    @ManyToOne
    @JoinColumn(name = "coupon_id", columnDefinition = "uuid", nullable = false)
    private Coupon coupon;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "target_value", nullable = false)
    private String targetValue;

}
