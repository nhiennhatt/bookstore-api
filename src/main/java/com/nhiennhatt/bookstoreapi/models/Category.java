package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends Base {

	@Column
	private String name;

	@Column(unique = true)
	private String slug;

	@Column(name = "thumb_img")
	private String thumbImg;

	@Column(name = "is_public")
	private boolean isPublic;

}
