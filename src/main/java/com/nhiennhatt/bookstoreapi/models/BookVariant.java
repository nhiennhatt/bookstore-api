package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_variants")
@Getter
@Setter
public class BookVariant extends Base {
	@Column
	private String name;

	@Column(unique = true)
	private String isbn;

	@Column
	private int originPrice;

	@Column
	private int salePrice;

	@Column
	private int inventory;

	@Column
	private String image;

	@ManyToOne
	@JoinColumn(name = "book_id", columnDefinition = "uuid")
	private Book book;
}
