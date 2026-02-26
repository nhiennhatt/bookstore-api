package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
public class Campaign extends Base {

	@Column
	private String name;

	@Column(unique = true)
	private String slug;

	@Column
	private int priority;

	@Column(name = "cover_img")
	private String coverImg;

	@Column(name = "thumb_img")
	private String thumbImg;

	@Column
	private String status;

	@Column(name = "theme_color")
	private String themeColor;

	@Column(name = "start_at")
	private Instant startAt;

	@Column
	private Instant endAt;

}
