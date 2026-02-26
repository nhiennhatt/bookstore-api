package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail extends Base {

    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "uuid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_variant_id", columnDefinition = "uuid", nullable = false)
    private BookVariant bookVariant;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

}
