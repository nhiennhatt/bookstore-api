package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends Base {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String slug;

    @Column(name = "thumb_img", length = 180)
    private String thumbImg;

    @Column(name = "is_public")
    private boolean isPublic = false;

}
