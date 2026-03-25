package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.validations.category.UpdateCategoryValidation;

import java.util.List;
import java.util.UUID;

public interface CustomCategoryRepository {
    List<Category> pagination(UUID cursor, int limit, Boolean isPublic);
    void partialUpdate(UpdateCategoryValidation category, UUID id);
}
