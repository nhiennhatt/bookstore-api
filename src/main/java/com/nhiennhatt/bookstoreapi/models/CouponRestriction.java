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
	@JoinColumn(name = "coupon_id", columnDefinition = "uuid")
	private Coupon coupon;

	@Column(name = "target_type")
	private String targetType;

	@Column(name = "target_value")
	private String targetValue;

}
