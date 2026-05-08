package com.nhiennhatt.bookstoreapi.validations.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderValidation {
    @Size(min = 1, max = 10)
    private List<CreateOrderItemValidation> variants;

    @NotNull
    private UUID addressId;
}
