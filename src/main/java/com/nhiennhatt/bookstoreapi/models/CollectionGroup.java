package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collection_groups")
@Getter
@Setter
public class CollectionGroup extends Base {

    @Column(nullable = false, length = 20)
    private String name;

    @Column
    private int priority;

    @ManyToOne
    @JoinColumn(name = "book_collection_id", columnDefinition = "uuid", nullable = false)
    private BookCollection bookCollection;

}
