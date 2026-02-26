package com.nhiennhatt.bookstoreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
public class UserAddress extends Base {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private User user;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(nullable = false, length = 60)
    private String district;

    @Column(nullable = false, length = 60)
    private String ward;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(name = "is_default")
    private boolean isDefault = false;

}
