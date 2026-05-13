package com.nhiennhatt.bookstoreapi.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDetailDto {
    private UUID id;
    private UUID variantId;
    private UUID bookId;
    private String bookName;
    private String bookSlug;
    private String variantName;
    private int quantity;
    private int originUnitPrice;
    private int unitPrice;
    private int totalPrice;
    private String image;
}
