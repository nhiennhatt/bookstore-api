package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "campaign_collections")
public class CampaignCollection extends Base {

	@Column
	private int coverImg;

	@ManyToOne
	@JoinColumn(name = "campaign_id", columnDefinition = "uuid")
	private BookCollection bookCollection;

	@ManyToOne
	@JoinColumn(name = "book_id", columnDefinition = "uuid")
	private Book book;

	@Column
	private int priority;

}
