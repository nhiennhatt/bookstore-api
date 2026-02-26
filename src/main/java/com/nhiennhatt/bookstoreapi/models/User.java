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
public class User extends Base {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 360)
    private String email;

    @Column(nullable = false, length = 180)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_role", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(length = 180)
    private String avatar;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(name = "verified_at")
    private Instant verifiedAt;

}
