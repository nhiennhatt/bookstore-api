package com.nhiennhatt.bookstoreapi.validations.bookCollection;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QueryBookCollectionValidation {
    private String keyword;
    private Boolean isPublic;

    @Min(0)
    private int page;

    @Min(1)
    @Max(10)
    private int limit = 5;
}
