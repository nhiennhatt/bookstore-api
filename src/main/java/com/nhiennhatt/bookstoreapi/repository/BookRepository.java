package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.dto.books.BookDetailDto;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomBookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, CustomBookRepository {
    @Modifying
    @Query("UPDATE Book b SET b.category = :category WHERE b.id = :id")
    void updateCategory(@Param("id") UUID id, @Param("category") Category category);

    Book getBookById(UUID id);

    Book getBookBySlug(String slug);

    @Query("""
                SELECT new com.nhiennhatt.bookstoreapi.dto.books.BookDetailDto(
                    b.id, b.name, b.author, b.publisher, b.distributor, b.slug, b.description, b.status, b.properties, b.image,
                    c, case when b.status = 'ACTIVE' then sum(coalesce(v.inventory, 0)) else 0 end
                )
                FROM Book b
                LEFT JOIN Category c ON b.categoryId = c.id
                LEFT JOIN BookVariant v ON b.id = v.bookId
                WHERE b.id = :id
                GROUP BY b, c
            """)
    BookDetailDto getBookDetailDtoById(@Param("id") UUID id);

    @Query("""
                SELECT new com.nhiennhatt.bookstoreapi.dto.books.BookDetailDto(
                    b.id, b.name, b.author, b.publisher, b.distributor, b.slug, b.description, b.status, b.properties, b.image,
                    c, case when b.status = 'ACTIVE' then sum(coalesce(v.inventory, 0)) else 0 end
                )
                FROM Book b
                LEFT JOIN Category c ON b.categoryId = c.id
                LEFT JOIN BookVariant v ON b.id = v.bookId and v.status = 'ACTIVE'
                WHERE b.slug = :slug
                GROUP BY b, c
            """)
    BookDetailDto getBookDetailDtoBySlug(String slug);
}
