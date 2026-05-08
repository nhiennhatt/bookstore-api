package com.nhiennhatt.bookstoreapi.validations.collectionBook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCollectionBookValidation {
    private int cursor;
    private int size;
    private String keyword;
    private boolean isExceptInActive;
}
