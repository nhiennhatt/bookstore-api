package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginValidation {
    @NotEmpty
    @Pattern(regexp = "^[a-z0-9_]{3,16}$")
    private String username;

    @NotEmpty
    private String password;
}
