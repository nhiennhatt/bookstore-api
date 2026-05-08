package com.nhiennhatt.bookstoreapi.validations.collectionBook;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCollectionBookValidation {
    @NotNull
    private UUID bookId;
}
