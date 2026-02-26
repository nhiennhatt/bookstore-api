package com.nhiennhatt.bookstoreapi.models;

import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "book_variants")
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "book_id", columnDefinition = "uuid", nullable = false)
    private Book book;
}
