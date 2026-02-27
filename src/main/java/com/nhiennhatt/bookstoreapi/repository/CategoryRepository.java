package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID> {
    @Override
    <S extends Category> S save(S entity);
}
