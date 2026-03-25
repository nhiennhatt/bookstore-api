package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;

import java.util.UUID;

public interface CustomBookVariantRepository {
    void partialUpdate(UUID id, UpdateBookVariant bookVariant);
}
