package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collection_groups")
@Getter
@Setter
public class CollectionGroup extends Base {

	@Column
	private String name;

	@Column
	private int priority;

	@ManyToOne
	@JoinColumn(name = "book_collection_id", columnDefinition = "uuid")
	private BookCollection bookCollection;

}
