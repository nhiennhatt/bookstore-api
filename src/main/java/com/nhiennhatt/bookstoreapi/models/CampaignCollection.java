package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "campaign_collections")
public class CampaignCollection extends Base {

    @Column(length = 180)
    private String coverImg;

    @ManyToOne
    @JoinColumn(name = "campaign_id", columnDefinition = "uuid", nullable = false)
    private BookCollection bookCollection;

    @ManyToOne
    @JoinColumn(name = "book_id", columnDefinition = "uuid", nullable = false)
    private Book book;

    @Column
    private int priority;

}
