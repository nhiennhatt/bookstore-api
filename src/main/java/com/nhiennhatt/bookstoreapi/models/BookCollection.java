package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collections")
@Getter
@Setter
public class BookCollection extends Base {

	@Column
	private String name;

	@Column(name = "is_public")
	private boolean isPublic;

	@Column
	private int priority;

	@Column(name = "is_campaign")
	private boolean isCampaign;
}
