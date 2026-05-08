package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.common.classes.BookQuery;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.dto.books.BookOverviewDto;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.utils.BookQueryBuilder;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public void partialUpdate(UUID id, UpdateBookValidation book) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Book> updateQuery = builder.createCriteriaUpdate(Book.class);
        Root<Book> root = updateQuery.from(Book.class);

        if (book.getName().isPresent()) {
            updateQuery.set(root.get("name"), book.getName().get());
        }

        if (book.getDescription().isPresent()) {
            updateQuery.set(root.get("description"), book.getDescription().get());
        }

        if (book.getAuthor().isPresent()) {
            updateQuery.set(root.get("author"), book.getAuthor().get());
        }

        if (book.getSlug().isPresent()) {
            updateQuery.set(root.get("slug"), book.getSlug().get());
        }

        if (book.getPublisher().isPresent()) {
            updateQuery.set(root.get("publisher"), book.getPublisher().get());
        }

        if (book.getStatus().isPresent()) {
            updateQuery.set(root.get("status"), book.getStatus().get());
        }

        if (book.getDistributor().isPresent()) {
            updateQuery.set(root.get("distributor"), book.getDistributor().get());
        }

        updateQuery.where(builder.equal(root.get("id"), id));
        Query query = entityManager.createQuery(updateQuery);
        int result = query.executeUpdate();
    }

    /*
     * Get book overview
     */
    @Override
    public List<BookOverviewDto> getBookOverviews(BookFilter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Book> book = query.from(Book.class);
        BookQuery bookQuery = BookQueryBuilder.create(builder, filter, book);

        query.select(builder.tuple(bookQuery.getSelections()))
                .where(bookQuery.getPredicates())
                .groupBy(bookQuery.getGroupByExpressions())
                .having(bookQuery.getHavingPredicates())
                .orderBy(builder.desc(book.get("id")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(filter.getLimit());

        List<Tuple> result = typedQuery.getResultList();

        return result.stream().map(t -> BookOverviewDto.builder()
                .id(t.get("id", UUID.class))
                .image(t.get("image", String.class))
                .name(t.get("name", String.class))
                .slug(t.get("slug", String.class))
                .categoryName(t.get("categoryName", String.class))
                .author(t.get("author", String.class))
                .publisher(t.get("publisher", String.class))
                .distributor(t.get("distributor", String.class))
                .totalStock(t.get("totalStock", Long.class))
                .validStock(t.get("validStock", Long.class))
                .variantStatus(t.get("variantStatus", BookVariantStatus.class))
                .status(t.get("status", BookStatus.class))
                .salePrice(t.get("salePrice", Integer.class))
                .build()).toList();
    }
}
