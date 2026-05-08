package com.nhiennhatt.bookstoreapi.validations.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateOrderItemValidation {
    @NotNull
    private UUID variantId;

    @NotNull
    @Min(1)
    @Max(10)
    private int quantity;
}
