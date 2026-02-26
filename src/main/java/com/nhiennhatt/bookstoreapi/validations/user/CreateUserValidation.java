package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserValidation {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[a-z0-9_]{3,16}$")
    private String username;

    @NotEmpty
    private String password;
}
