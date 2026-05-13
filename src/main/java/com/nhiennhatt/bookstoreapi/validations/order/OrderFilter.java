package com.nhiennhatt.bookstoreapi.validations.order;

import com.nhiennhatt.bookstoreapi.common.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderFilter {
    private UUID cursor;
    private OrderStatus status;
    private String paymentCode;
    private String deliveryCode;
    private Integer districtId;
    private Integer provinceId;
    private String wardCode;
    @Min(10)
    private int limit;
    private Instant startDate;
    private Instant endDate;
}
