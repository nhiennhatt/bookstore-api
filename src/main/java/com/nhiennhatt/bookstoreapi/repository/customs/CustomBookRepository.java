package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;

import java.util.List;
import java.util.UUID;

public interface CustomBookRepository {
    void partialUpdate(UUID id, UpdateBookValidation book);
    List<Book> getBooks(BookFilter bookFilter);
}
