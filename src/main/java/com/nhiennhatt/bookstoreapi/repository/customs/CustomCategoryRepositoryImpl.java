package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.validations.category.UpdateCategoryValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Category> pagination(UUID cursor, int limit, Boolean isPublic) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);
        List<Predicate> predicates = new ArrayList<>();
        if (cursor != null) {
            predicates.add(cb.lessThan(root.get("id"), cursor));
        }
        if (isPublic != null) {
            predicates.add(cb.equal(root.get("isPublic"), isPublic));
        }
        query.select(root)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(root.get("id")));
        TypedQuery<Category> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit);

        return typedQuery.getResultList();
    }

    @Override
    public int partialUpdate(UpdateCategoryValidation category, String slug) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Category> updateQuery = cb.createCriteriaUpdate(Category.class);
        Root<Category> root = updateQuery.from(Category.class);
        if (category.getName().isPresent()) {
            updateQuery.set(root.get("name"), category.getName().get());
        }

        if (category.getIsPublic().isPresent()) {
            updateQuery.set(root.get("isPublic"), category.getIsPublic().get());
        }

        updateQuery.where(cb.equal(root.get("slug"), slug));
        return entityManager.createQuery(updateQuery).executeUpdate();
    }
}
