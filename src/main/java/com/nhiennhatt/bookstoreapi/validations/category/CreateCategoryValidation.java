package com.nhiennhatt.bookstoreapi.validations.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryValidation {
    @NotEmpty
    private String name;

    @NotNull
    private boolean isPublic = true;
}
