package com.nhiennhatt.bookstoreapi.validations.book;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateBookCategory {
    @NotNull
    private UUID categoryId;
}
