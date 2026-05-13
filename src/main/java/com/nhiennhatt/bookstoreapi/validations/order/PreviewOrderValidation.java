package com.nhiennhatt.bookstoreapi.validations.order;

import com.nhiennhatt.bookstoreapi.common.interfaces.CreatingOrder;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PreviewOrderValidation implements CreatingOrder {
    @Size(min = 1, max = 10)
    private List<CreateOrderItemValidation> variants;

    private UUID addressId;
}
