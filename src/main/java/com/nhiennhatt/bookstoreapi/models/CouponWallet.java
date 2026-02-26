package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coupon_wallets")
@Getter
@Setter
public class CouponWallet extends Base {

	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "uuid")
	private User user;

	@ManyToOne
	@JoinColumn(name = "coupon_id", columnDefinition = "uuid")
	private Coupon coupon;

}
