package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail extends Base {
    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "uuid", nullable = false)
    private Order order;

    @Column(name = "order_id", insertable = false, updatable = false)
    private UUID orderId;

    @Column(name = "book_variant_id", insertable = false, updatable = false)
    private UUID bookVariantId;

    @ManyToOne
    @JoinColumn(name = "book_variant_id", columnDefinition = "uuid", nullable = false)
    private BookVariant bookVariant;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "origin_unit_price")
    private int originUnitPrice;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;
}
