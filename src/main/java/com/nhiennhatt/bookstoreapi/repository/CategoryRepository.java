package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomCategoryRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID>, CustomCategoryRepository {
    @Override
    <S extends Category> S save(S entity);

    Category findCategoryBySlug(String slug);
    Category findCategoryById(UUID id);
}
