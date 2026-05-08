package com.nhiennhatt.bookstoreapi.repository.projection;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookWithVariantForOrderProjection {
    private UUID bookId;
    private String bookName;
    private UUID variantId;
    private String variantName;
    private BookStatus bookStatus;
    private BookVariantStatus variantStatus;
    private int inventory;
    private int originPrice;
    private int salePrice;
    private int weight;
}
