package com.nhiennhatt.bookstoreapi.validations.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryValidation {
    @NotEmpty
    private String name;

    @Size(min = 8, max = 80)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
    private String slug;

    @NotNull
    private boolean isPublic = true;

    @NotNull
    private boolean isFeatured = false;
}
