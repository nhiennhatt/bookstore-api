package com.nhiennhatt.bookstoreapi.dto.bookcollections;

import com.nhiennhatt.bookstoreapi.dto.books.BookOverviewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CollectionBookOverviewDto {
    private UUID id;
    private UUID collectionId;
    private long position;
    private String collectionName;
    private BookOverviewDto book;
}
