package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
public class Campaign extends Base {

    @Column(nullable = false, length = 160)
    private String name;

    @Column(unique = true, nullable = false, length = 170)
    private String slug;

    @Column
    private int priority;

    @Column(name = "cover_img", length = 180)
    private String coverImg;

    @Column(name = "thumb_img", length = 180)
    private String thumbImg;

    @Column(nullable = false)
    private String status;

    @Column(name = "theme_color", length = 8)
    private String themeColor;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

}
