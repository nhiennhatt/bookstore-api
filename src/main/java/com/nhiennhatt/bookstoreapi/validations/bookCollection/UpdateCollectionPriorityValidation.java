package com.nhiennhatt.bookstoreapi.validations.bookCollection;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCollectionPriorityValidation {
    @NotNull
    @Min(1)
    private int priority;
}
