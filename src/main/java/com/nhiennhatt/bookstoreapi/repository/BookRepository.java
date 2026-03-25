package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomBookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, CustomBookRepository {
    @Modifying
    @Query("UPDATE Book b SET b.category = :category WHERE b.id = :id")
    void updateCategory(@Param("id") UUID id, @Param("category") Category category);

    Book getBookById(UUID id);

    Book getBookBySlug(String slug);
}
