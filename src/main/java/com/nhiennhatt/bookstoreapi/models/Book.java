package com.nhiennhatt.bookstoreapi.models;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book extends Base {
    @Column(nullable = false, length = 160)
    private String name;

    @Column(length = 80)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(length = 100)
    private String distributor;

    @Column(unique = true, nullable = false, length = 170)
    private String slug;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "book_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private BookStatus status;

    @Column
    private String properties;

    @Column(length = 180)
    private String image;
}
