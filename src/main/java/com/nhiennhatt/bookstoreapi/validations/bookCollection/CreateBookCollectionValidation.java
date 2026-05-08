package com.nhiennhatt.bookstoreapi.validations.bookCollection;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookCollectionValidation {
    @NotNull
    private String name;

    @NotNull
    private boolean isPublic;
}
