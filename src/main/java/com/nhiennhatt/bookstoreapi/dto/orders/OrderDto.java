package com.nhiennhatt.bookstoreapi.dto.orders;

import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class OrderDto extends OrderOverviewDto {
    private List<OrderDetailDto> orderDetails;

    public OrderDto(UUID id, MeResponse user, UserAddress address, OrderStatus status, int subtotalPrice, int shippingFee, int orderDiscount, int shippingDiscount, int grandTotal, List<OrderDetailDto> orderDetails) {
        super(id, user, address, status, subtotalPrice, shippingFee, orderDiscount, shippingDiscount, grandTotal);
        this.orderDetails = orderDetails;
    }

    public OrderDto(OrderOverviewDto orderOverviewDto, List<OrderDetailDto> orderDetails) {
        super(
                orderOverviewDto.getId(),
                orderOverviewDto.getUser(),
                orderOverviewDto.getAddress(),
                orderOverviewDto.getStatus(),
                orderOverviewDto.getSubtotalPrice(),
                orderOverviewDto.getShippingFee(),
                orderOverviewDto.getOrderDiscount(),
                orderOverviewDto.getShippingDiscount(),
                orderOverviewDto.getGrandTotal()
        );
        this.orderDetails = orderDetails;
    }
}
