package com.nhiennhatt.bookstoreapi.validations.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookFilter {
    private UUID categoryId;
    private UUID cursor;
    private Integer limit = 10;
}
