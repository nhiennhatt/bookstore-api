package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomBookVariantRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookVariantRepository extends JpaRepository<BookVariant, UUID>, CustomBookVariantRepository {
    List<BookVariant> findByBookId(UUID bookId);
    BookVariant findBookVariantById(UUID id);
    List<BookVariant> findBookVariantsByBook(Book book);
}
