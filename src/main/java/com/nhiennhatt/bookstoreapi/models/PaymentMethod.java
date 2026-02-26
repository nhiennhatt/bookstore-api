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
    @Column
    private String name;

    @Column(unique = true)
    private String code;

    @Column
    private String icon;

    @Column
    private boolean isActive = false;

    @Column
    private int priority;

    @Column
    private String config;

    @Column
    private String type;
}
