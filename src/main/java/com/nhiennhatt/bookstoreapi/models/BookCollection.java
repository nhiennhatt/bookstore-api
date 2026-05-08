package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collections")
@Getter
@Setter
public class BookCollection extends Base {

    @Column(nullable = false)
    private String name;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @Column
    private int priority = 100;
}
