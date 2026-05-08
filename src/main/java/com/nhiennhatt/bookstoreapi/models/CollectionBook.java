package com.nhiennhatt.bookstoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(
        name = "collection_books",
        indexes = {
                @Index(columnList = "book_id, collection_id", unique = true),
                @Index(columnList = "book_id, position", unique = true)
        }
)
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "collection"})
public class CollectionBook extends Base {
    @Column(name = "book_id", insertable = false, updatable = false)
    private UUID bookId;

    @Column(name = "collection_id", insertable = false, updatable = false)
    private UUID collectionId;

    @Column(nullable = false)
    private long position;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "collection_id", nullable = false)
    private BookCollection collection;
}
