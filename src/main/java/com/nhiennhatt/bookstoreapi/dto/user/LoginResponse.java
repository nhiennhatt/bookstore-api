package com.nhiennhatt.bookstoreapi.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
}
