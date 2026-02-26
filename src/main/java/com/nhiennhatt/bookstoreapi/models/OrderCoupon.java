package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_coupons")
@Getter
@Setter
public class OrderCoupon extends Base {

	@ManyToOne
	private Order order;

	@ManyToOne
	private Coupon coupon;

	@Column
	private String type;

	@Column(name = "discount_amount")
	private int discountAmount;

}
