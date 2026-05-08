package com.nhiennhatt.bookstoreapi.dto.books;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookDetailDto {
    private UUID id;

    private String name;

    private String author;

    private String publisher;

    private String distributor;

    private String slug;

    private String description;

    private BookStatus status;

    private String properties;

    private String image;

    private Category category;

    private long stock;
}
