package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
public class PaymentMethod extends Base {
    @Column(nullable = false, length = 15)
    private String name;

    @Column(length = 180)
    private String icon;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column
    private int priority;

    @Column
    private String config;

    @Column(nullable = false)
    private String type;
}
