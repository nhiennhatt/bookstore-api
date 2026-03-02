package com.nhiennhatt.bookstoreapi.common.classes;

import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class CurrentUser {
    private UUID id;
    private String username;
    private UserRole role;
    private UserStatus status;
    private String password;
}
