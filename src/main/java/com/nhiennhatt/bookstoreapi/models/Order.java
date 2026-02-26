package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends Base {

	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "uuid")
	private User user;

	@ManyToOne
	@JoinColumn(name = "address_id", columnDefinition = "uuid")
	private UserAddress address;

	@Column
	private String status;

	@Column(name = "subtotal_price")
	private int subtotalPrice;

	@Column(name = "shipping_fee")
	private int shippingFee;

	@Column(name = "order_discount")
	private int orderDiscount;

	@Column(name = "shipping_discount")
	private int shippingDiscount;

	@Column(name = "grand_total")
	private int grandTotal;

	@ManyToOne
	@JoinColumn(name = "payment_method_id", columnDefinition = "uuid")
	private PaymentMethod paymentMethod;

}
