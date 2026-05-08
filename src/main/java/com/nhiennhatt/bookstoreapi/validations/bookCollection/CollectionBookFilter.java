package com.nhiennhatt.bookstoreapi.validations.bookCollection;

import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CollectionBookFilter extends BookFilter {
    private Integer positionCursor;
}
