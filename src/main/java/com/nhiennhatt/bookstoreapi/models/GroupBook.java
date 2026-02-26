package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "group_books")
@Getter
@Setter
public class GroupBook extends Base {

	@ManyToOne
	@JoinColumn(name = "group_id", columnDefinition = "uuid")
	private CollectionGroup group;

	@ManyToOne
	@JoinColumn(name = "book_id", columnDefinition = "uuid")
	private Book book;

}
