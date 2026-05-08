package com.nhiennhatt.bookstoreapi.dto.user;

import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.common.enums.UserStatus;
import com.nhiennhatt.bookstoreapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class MeResponse {
    public MeResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.avatar = user.getAvatar();
        this.verified = user.isVerified();
    }

    private final String username;

    private final String email;

    private final UserRole role;

    private final UserStatus status;

    private final String firstName;

    private final String lastName;

    private final String avatar;

    private final boolean verified;
}
