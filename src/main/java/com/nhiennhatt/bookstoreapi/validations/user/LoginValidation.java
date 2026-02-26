package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginValidation {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
