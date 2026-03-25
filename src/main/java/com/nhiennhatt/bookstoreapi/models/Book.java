package com.nhiennhatt.bookstoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category"})
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column
    private String properties;

    @Column(length = 180)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "category_id", insertable = false, updatable = false)
    private UUID categoryId;
}
