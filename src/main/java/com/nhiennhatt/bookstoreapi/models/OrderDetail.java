package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail extends Base {

	@ManyToOne
	@JoinColumn(name = "order_id", columnDefinition = "uuid")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "book_variant_id", columnDefinition = "uuid")
	private BookVariant bookVariant;

	@Column
	private int quantity;

	@Column(name = "unit_price")
	private int unitPrice;

	@Column(name = "total_price")
	private int totalPrice;

}
