package com.nhiennhatt.bookstoreapi.validations.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswordValidation {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Length(min = 6, max = 20)
    private String newPassword;
}
