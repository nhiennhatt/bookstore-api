package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.dto.category.CreateCategoryResponse;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.CategoryRepository;
import com.nhiennhatt.bookstoreapi.utils.RandomText;
import com.nhiennhatt.bookstoreapi.utils.Slugify;
import com.nhiennhatt.bookstoreapi.validations.category.CreateCategoryValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CreateCategoryResponse createCategory(CreateCategoryValidation category) {
        String slug = Slugify.slugify(category.getName(), 70) + "-" + RandomText.randomText(9);
        Category categoryEntity = Category.builder()
                .name(category.getName())
                .slug(slug)
                .isPublic(category.isPublic())
                .build();
        categoryRepository.save(categoryEntity);
        return CreateCategoryResponse.builder().id(categoryEntity.getId()).slug(slug).build();
    }
}
