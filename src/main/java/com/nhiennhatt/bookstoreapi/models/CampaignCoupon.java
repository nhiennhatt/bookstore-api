package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "campaign_coupons")
@Entity
@Getter
@Setter
public class CampaignCoupon extends Base {
	@ManyToOne
	@JoinColumn(name = "campaign_id", columnDefinition = "uuid")
	private Campaign campaign;

	@ManyToOne
	@JoinColumn(name = "coupon_id", columnDefinition = "uuid")
	private Coupon coupon;

}
