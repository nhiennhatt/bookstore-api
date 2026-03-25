package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Book> getBooks(BookFilter bookFilter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        query.select(root);
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (bookFilter.getCategoryId() != null) {
            predicates.add(builder.equal(root.get("category").get("id"), bookFilter.getCategoryId()));
        }

        if (bookFilter.getCursor() != null) {
            predicates.add(builder.lessThan(root.get("id"), bookFilter.getCursor()));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        query.orderBy(builder.desc(root.get("id")));
        TypedQuery<Book> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(bookFilter.getLimit() != null ? bookFilter.getLimit() : 10);
        return typedQuery.getResultList();
    }
}
