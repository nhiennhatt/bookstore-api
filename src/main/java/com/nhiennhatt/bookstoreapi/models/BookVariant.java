package com.nhiennhatt.bookstoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "book_variants")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "book"})
public class BookVariant extends Base {
    @Column(nullable = false, length = 80)
    private String name;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @Column(name = "origin_price", nullable = false)
    private int originPrice;

    @Column(name = "sale_price")
    private int salePrice;

    @Column
    private int inventory = 0;

    @Column(length = 180)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "book_variant_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private BookVariantStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "book_id", columnDefinition = "uuid", nullable = false)
    private Book book;

    @Column(name = "book_id", insertable = false, updatable = false)
    private UUID bookId;
}
