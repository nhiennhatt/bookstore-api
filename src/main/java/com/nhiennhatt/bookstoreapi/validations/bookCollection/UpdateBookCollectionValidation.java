package com.nhiennhatt.bookstoreapi.validations.bookCollection;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookCollectionValidation {
    @NotBlank
    private String name;

    private boolean isPublic;
}
