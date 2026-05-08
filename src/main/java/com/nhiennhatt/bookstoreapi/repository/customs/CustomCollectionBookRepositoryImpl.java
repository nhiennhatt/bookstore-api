package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.common.classes.BookQuery;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.dto.bookcollections.CollectionBookOverviewDto;
import com.nhiennhatt.bookstoreapi.dto.books.BookOverviewDto;
import com.nhiennhatt.bookstoreapi.models.*;
import com.nhiennhatt.bookstoreapi.utils.BookQueryBuilder;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.CollectionBookFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomCollectionBookRepositoryImpl implements CustomCollectionBookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CollectionBookOverviewDto> getCollectionBooksByCollectionId(
            @NotNull UUID collectionId, CollectionBookFilter filter
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<CollectionBook> collectionBook = query.from(CollectionBook.class);
        Join<CollectionBook, Book> book = collectionBook.join("book");

        BookQuery bookQuery = BookQueryBuilder.create(builder, filter, book);

        bookQuery.getPredicates().add(0, builder.equal(collectionBook.get("collectionId"), collectionId));

        if (filter.getPositionCursor() != null && filter.getPositionCursor() > 0) {
            bookQuery.getPredicates().add(1, builder.greaterThan(collectionBook.get("position"), filter.getPositionCursor()));
        }

        bookQuery.getSelections().addAll(0, List.of(
                collectionBook.get("id").alias("collectionBookId"),
                collectionBook.get("collectionId").alias("collectionId"),
                collectionBook.get("position").alias("bookPosition")
        ));

        bookQuery.getGroupByExpressions().addAll(0, List.of(
                collectionBook.get("id"),
                collectionBook.get("collectionId"),
                collectionBook.get("position")
        ));

        query.select(builder.tuple(bookQuery.getSelections()))
                .where(bookQuery.getPredicates())
                .groupBy(bookQuery.getGroupByExpressions())
                .having(bookQuery.getHavingPredicates())
                .orderBy(builder.asc(collectionBook.get("position")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);

        typedQuery.setMaxResults(filter.getLimit());

        return typedQuery.getResultList().stream().map((t) -> {
            BookOverviewDto bookOverview = BookOverviewDto.builder()
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
                    .build();

            return CollectionBookOverviewDto.builder()
                    .id(t.get("collectionBookId", UUID.class))
                    .collectionId(t.get("collectionId", UUID.class))
                    .position(t.get("bookPosition", Long.class))
                    .book(bookOverview)
                    .build();
        }).toList();
    }

    @Override
    public int updateCollectionBookPosition(UUID collectionId, UUID bookId, long destination) {
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

        long currentCount = countCollectionBooksByCollectionId(collectionId);

        if (currentCount == 0 || currentCount < destination) return 0;

        CollectionBook collectionBook = getCollectionBookByCollectionIdAndBookId(collectionId, bookId);

        if (collectionBook == null || collectionBook.getPosition() == destination) return 0;

        long currentPosition = collectionBook.getPosition();

        if (destination < currentPosition) {
            entityManager
                    .createQuery(
                            "UPDATE CollectionBook cb SET cb.position = cb.position + 1 " +
                                    "WHERE cb.collectionId = :collectionId AND cb.position < :currentPosition AND cb.position >= :destination"
                    )
                    .setParameter("currentPosition", currentPosition)
                    .setParameter("collectionId", collectionId)
                    .setParameter("destination", destination)
                    .executeUpdate();
        } else {
            entityManager
                    .createQuery(
                            "UPDATE CollectionBook cb SET cb.position = cb.position - 1 " +
                                    "WHERE cb.collectionId = :collectionId AND cb.position <= :destination AND cb.position > :currentPosition"
                    )
                    .setParameter("currentPosition", currentPosition)
                    .setParameter("destination", destination)
                    .setParameter("collectionId", collectionId)
                    .executeUpdate();
        }

        int updatedResult = (
                entityManager.createQuery("UPDATE CollectionBook cb SET cb.position = :position WHERE cb.id = :id")
                        .setParameter("position", destination)
                        .setParameter("id", collectionBook.getId())
                        .executeUpdate()
        );

        if (updatedResult == 0) return 0;

        return 1;
    }

    @Override
    public int deleteCollectionBook(UUID collectionId, UUID bookId) {
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

        long currentCount = countCollectionBooksByCollectionId(collectionId);

        if (currentCount == 0) return 0;

        CollectionBook collectionBook = getCollectionBookByCollectionIdAndBookId(collectionId, bookId);
        System.out.println(collectionBook == null);

        if (collectionBook == null) return 0;

        long currentPosition = collectionBook.getPosition();

        entityManager.remove(collectionBook);

        if (currentPosition < currentCount) {
            entityManager
                    .createQuery(
                            "UPDATE CollectionBook SET position = position - 1 WHERE position > :currentPosition"
                    )
                    .setParameter("currentPosition", currentPosition)
                    .executeUpdate();
        }

        return 1;
    }

    private long countCollectionBooksByCollectionId(UUID collectionId) {
        return entityManager.createQuery(
                "SELECT count(c) from CollectionBook c WHERE collectionId = :collectionId",
                Long.class
        ).setParameter("collectionId", collectionId).getSingleResult();
    }

    private CollectionBook getCollectionBookByCollectionIdAndBookId(UUID collectionId, UUID bookId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<CollectionBook> collectionBookCriQuery = builder.createQuery(CollectionBook.class);
        Root<CollectionBook> collectionBookRoot = collectionBookCriQuery.from(CollectionBook.class);
        collectionBookCriQuery.where(builder.and(
                builder.equal(collectionBookRoot.get("collectionId"), collectionId),
                builder.equal(collectionBookRoot.get("bookId"), bookId)
        ));

        return entityManager.createQuery(collectionBookCriQuery).getSingleResultOrNull();
    }
}
