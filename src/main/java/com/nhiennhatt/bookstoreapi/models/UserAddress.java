package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
public class UserAddress extends Base {

	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "uuid")
	private User user;

	@Column
	private String city;

	@Column
	private String district;

	@Column
	private String ward;

	@Column
	private String address;

	@Column(name = "is_default")
	private boolean isDefault;

}
