package com.nhiennhatt.bookstoreapi.dto.orders;

import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class OrderOverviewDto {
    private UUID id;
    private MeResponse user;
    private UserAddress address;
    private OrderStatus status;
    private int subtotalPrice;
    private int shippingFee;
    private int orderDiscount;
    private int shippingDiscount;
    private int grandTotal;
}
