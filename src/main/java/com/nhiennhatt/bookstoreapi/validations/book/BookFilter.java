package com.nhiennhatt.bookstoreapi.validations.book;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class BookFilter {
    private UUID categoryId;
    private UUID cursor;
    private String keyword;
    private int limit;
    private BookStatus bookStatus;
    private BookVariantStatus variantStatus;
    private Boolean isStockValid;
}
