package com.nhiennhatt.bookstoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
public class UserAddress extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private User user;

    @Column(nullable = false, length = 60)
    private String province;

    @Column(nullable = false, length = 30)
    private String district;

    @Column(nullable = false, length = 60)
    private String ward;

    @Column(nullable = false, name = "province_id" )
    private int provinceId;

    @Column(nullable = false, name = "district_id")
    private int districtId;

    @Column(nullable = false, length = 12, name = "ward_code")
    private String wardCode;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_default")
    private boolean isDefault = false;

}
