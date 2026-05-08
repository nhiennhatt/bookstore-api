package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTokenValidation {
    @NotNull
    private String refreshToken;
}
