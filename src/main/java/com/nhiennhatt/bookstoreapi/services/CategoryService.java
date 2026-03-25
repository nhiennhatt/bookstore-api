package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.dto.category.CreateCategoryResponse;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.CategoryRepository;
import com.nhiennhatt.bookstoreapi.utils.MimeTypeUtil;
import com.nhiennhatt.bookstoreapi.utils.RandomText;
import com.nhiennhatt.bookstoreapi.utils.Slugify;
import com.nhiennhatt.bookstoreapi.validations.category.CreateCategoryValidation;
import com.nhiennhatt.bookstoreapi.validations.category.UpdateCategoryValidation;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MinioService minioService;

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

    @Transactional(rollbackFor = Exception.class)
    public void uploadCategoryImage(UUID id, MultipartFile file) {
        Category category = categoryRepository.findCategoryById(id);
        if (category == null)
            throw new AppException("Category not found", "CATEGORY_NOT_FOUND", 404, null, null);

        try {
            if (category.getThumbImg() != null) {
                minioService.deleteFile(category.getThumbImg());
            }

            String extension = MimeTypeUtil.getExtension(file.getInputStream());

            String fileName = String.format("categories/%s-%s%s", category.getSlug(), RandomText.randomText(9), extension);
            minioService.uploadFile(file, fileName);

            category.setThumbImg(fileName);
            categoryRepository.save(category);
        } catch (Exception e) {
            logger.error("Error uploading category image: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryBySlug(String slug, CurrentUser currentUser) {
        Category category = categoryRepository.findCategoryBySlug(slug);
        if (category == null
                || (!category.isPublic() && (currentUser == null || currentUser.getRole() == UserRole.ROLE_CUSTOMER))
        )
            throw new AppException("Category not found", "CATEGORY_NOT_FOUND", 404, null, null);

        if (category.getThumbImg() != null) {
            try {
                category.setThumbImg(minioService.getPresignedUrl(category.getThumbImg()));
            } catch (Exception e) {
                logger.error("Error generating presigned URL: {}", e.getMessage());
                category.setThumbImg(null);
            }
        }
        return category;
    }

    public Category getCategoryById(UUID id, CurrentUser currentUser) {
        Category category = categoryRepository.findCategoryById(id);
        if (category == null
                || (!category.isPublic() && (currentUser == null || currentUser.getRole() == UserRole.ROLE_CUSTOMER))
        )
            throw new AppException("Category not found", "CATEGORY_NOT_FOUND", 404, null, null);

        if (category.getThumbImg() != null) {
            try {
                category.setThumbImg(minioService.getPresignedUrl(category.getThumbImg()));
            } catch (Exception e) {
                logger.error("Error generating presigned URL: {}", e.getMessage());
                category.setThumbImg(null);
            }
        }
        return category;
    }

    public List<Category> getAllCategories(UUID cursor, Integer limit, Boolean isPublic, CurrentUser currentUser) {
        if (currentUser == null || currentUser.getRole() == UserRole.ROLE_CUSTOMER) {
            isPublic = true;
        }
        List<Category> categories = categoryRepository.pagination(cursor, limit, isPublic);
        categories = categories.stream().peek((category -> {
            if (category.getThumbImg() != null) {
                try {
                    category.setThumbImg(minioService.getPresignedUrl(category.getThumbImg()));
                } catch (Exception e) {
                    logger.error("Error generating presigned URL: {}", e.getMessage());
                    category.setThumbImg(null);
                }
            }
        })).toList();

        return categories;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(UUID id, UpdateCategoryValidation category) {
        categoryRepository.partialUpdate(category, id);
    }
}
