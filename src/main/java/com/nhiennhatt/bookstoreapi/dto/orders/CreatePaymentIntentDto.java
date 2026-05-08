package com.nhiennhatt.bookstoreapi.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePaymentIntentDto {
    private String clientSecret;
}
