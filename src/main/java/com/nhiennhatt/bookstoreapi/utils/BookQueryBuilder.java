package com.nhiennhatt.bookstoreapi.utils;

import com.nhiennhatt.bookstoreapi.common.classes.BookQuery;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class BookQueryBuilder {
    static public BookQuery create(CriteriaBuilder builder, BookFilter filter, From<?, Book> book) {
        List<Selection<?>> selections = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> groupByPredicates = new ArrayList<>();
        List<Expression<?>> groupBys = new ArrayList<>();

        if (filter.getCursor() != null) {
            predicates.add(builder.lessThan(book.get("id"), filter.getCursor()));
        }

        if (filter.getCategoryId() != null) {
            predicates.add(builder.equal(book.get("categoryId"), filter.getCategoryId()));
        }

        if (filter.getBookStatus() != null) {
            predicates.add(builder.equal(book.get("status"), filter.getBookStatus()));
        }

        Join<Book, BookVariant> variant = book.join("variants", JoinType.LEFT);

        Expression<Long> validStockExpression = builder.sum(
                builder.<Long>selectCase()
                        .when(builder.equal(variant.get("status"), BookVariantStatus.ACTIVE), variant.get("inventory"))
                        .otherwise(0L)
        );

        Expression<Long> totalStockExpression = builder.sum((variant.get("inventory"))).cast(Long.class);

        Expression<BookVariantStatus> variantStatusExpression = builder.<BookVariantStatus>selectCase()
                .when(builder.greaterThan(builder.count(
                        builder.selectCase()
                                .when(builder.equal(variant.get("status"), BookVariantStatus.ACTIVE), 1)
                                .otherwise(0L)
                ), 0L), BookVariantStatus.ACTIVE)
                .otherwise(BookVariantStatus.INACTIVE)
                .cast(BookVariantStatus.class);

        Expression<Integer> minSalePriceExpress = builder.min(
                builder.<Integer>selectCase()
                        .when(builder.equal(variant.get("status"), BookVariantStatus.ACTIVE), variant.get("salePrice"))
                        .otherwise((Integer) null)
                        .as(Integer.class)
        );

        if (filter.getVariantStatus() != null) {
            groupByPredicates.add(builder.equal(variantStatusExpression, filter.getVariantStatus()));
        }

        if (filter.getIsStockValid() != null) {
            groupByPredicates.add(
                    filter.getIsStockValid()
                            ? builder.greaterThan(validStockExpression, 0L)
                            : builder.lessThanOrEqualTo(validStockExpression, 0L)
            );
        }

        Join<Book, Category> category = book.join("category", JoinType.LEFT);

        if (filter.getKeyword() != null) {
            predicates.add(builder.or(
                    builder.like((builder.lower(book.get("name"))), "%" + filter.getKeyword().toLowerCase() + "%"),
                    builder.like(builder.lower(book.get("description")), "%" + filter.getKeyword().toLowerCase() + "%"),
                    builder.like(builder.lower(variant.get("isbn")), "%" + filter.getKeyword().toLowerCase() + "%"),
                    builder.like(builder.lower(category.get("name")), "%" + filter.getKeyword().toLowerCase() + "%")
            ));
        }

        selections.addAll(List.of(
                book.get("id").alias("id"),
                book.get("name").alias("name"),
                book.get("image").alias("image"),
                book.get("author").alias("author"),
                book.get("publisher").alias("publisher"),
                book.get("distributor").alias("distributor"),
                book.get("slug").alias("slug"),
                book.get("status").alias("status"),
                category.get("name").alias("categoryName"),
                validStockExpression.alias("validStock"),
                totalStockExpression.alias("totalStock"),
                variantStatusExpression.alias("variantStatus"),
                minSalePriceExpress.alias("salePrice")
        ));

        groupBys.addAll(List.of(
                book.get("id"),
                book.get("name"),
                book.get("image"),
                book.get("author"),
                book.get("publisher"),
                book.get("distributor"),
                book.get("slug"),
                book.get("status"),
                category.get("name")
        ));

        return BookQuery.builder()
                .selections(selections)
                .predicates(predicates)
                .havingPredicates(groupByPredicates)
                .groupByExpressions(groupBys)
                .build();
    }
}
