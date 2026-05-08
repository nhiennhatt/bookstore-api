package com.nhiennhatt.bookstoreapi.dto.books;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class BookOverviewDto {
    private UUID id;

    private String name;

    private String author;

    private String publisher;

    private String distributor;

    private String slug;

    private BookStatus status;

    private String image;

    private String categoryName;

    private int salePrice;

    private long validStock;

    private long totalStock;

    private BookVariantStatus variantStatus;
}
