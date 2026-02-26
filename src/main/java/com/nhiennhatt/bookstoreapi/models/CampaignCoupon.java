package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "campaign_coupons")
@Entity
@Getter
@Setter
public class CampaignCoupon extends Base {
    @ManyToOne
    @JoinColumn(name = "campaign_id", columnDefinition = "uuid", nullable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "coupon_id", columnDefinition = "uuid", nullable = false)
    private Coupon coupon;

}
