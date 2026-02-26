package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book extends Base {
	@Column
	private String name;

	@Column
	private String author;

	@Column
	private String publisher;

	@Column
	private String distributor;

	@Column(unique = true)
	private String slug;

	@Column
	private String description;

	@Column(name = "is_public")
	private boolean isPublic;

	@Column
	private boolean verified;

	@Column
	private String status;

	@Column
	private String properties;

	@Column
	private String image;

	@Column(name = "verified_at")
	private Instant verifiedAt;

}
