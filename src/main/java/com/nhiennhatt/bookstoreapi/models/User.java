package com.nhiennhatt.bookstoreapi.models;

import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends Base{

	@Column(unique = true)
	private String username;

	@Column(unique = true)
	private String email;

	@Column
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "user_role")
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "user_status")
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private UserStatus status = UserStatus.ACTIVE;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column
	private String avatar;

	@Column
	private boolean verified = false;

	@Column(name = "verified_at")
	private Instant verifiedAt;

}
