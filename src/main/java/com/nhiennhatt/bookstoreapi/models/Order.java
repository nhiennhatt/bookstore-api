package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends Base {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", columnDefinition = "uuid", nullable = false)
    private UserAddress address;

    @Column(nullable = false)
    private String status;

    @Column(name = "subtotal_price", nullable = false)
    private int subtotalPrice;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "order_discount")
    private int orderDiscount;

    @Column(name = "shipping_discount")
    private int shippingDiscount;

    @Column(name = "grand_total", nullable = false)
    private int grandTotal;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", columnDefinition = "uuid", nullable = false)
    private PaymentMethod paymentMethod;

}
