package com.nhiennhatt.bookstoreapi.common.interfaces;


import com.nhiennhatt.bookstoreapi.validations.order.CreateOrderItemValidation;

import java.util.List;
import java.util.UUID;

public interface CreatingOrder {
    List<CreateOrderItemValidation> getVariants();
    UUID getAddressId();
}
