package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserValidation {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
