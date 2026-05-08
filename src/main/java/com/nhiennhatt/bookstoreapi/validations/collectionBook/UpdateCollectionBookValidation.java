package com.nhiennhatt.bookstoreapi.validations.collectionBook;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCollectionBookValidation {
    @Min(1)
    private long position;
}
