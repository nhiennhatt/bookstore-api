package com.nhiennhatt.bookstoreapi.validations.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CategoriesFilter {
    private UUID cursor = null;

    @Max(40)
    private Integer limit = 10;

    private Boolean isPublic = null;
}
