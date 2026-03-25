package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CustomBookVariantRepositoryImpl implements CustomBookVariantRepository{
    @Autowired
    private EntityManager entityManager;

    @Override
    public void partialUpdate(UUID id, UpdateBookVariant bookVariant) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<BookVariant> updateQuery = builder.createCriteriaUpdate(BookVariant.class);
        Root<BookVariant> root = updateQuery.from(BookVariant.class);
        if (bookVariant.getName().isPresent()) {
            updateQuery.set(root.get("name"), bookVariant.getName().get());
        }

        if (bookVariant.getStatus().isPresent()) {
            updateQuery.set(root.get("status"), bookVariant.getStatus().get());
        }

        if (bookVariant.getInventory().isPresent()) {
            updateQuery.set(root.get("inventory"), bookVariant.getInventory().get());
        }

        if (bookVariant.getOriginPrice().isPresent()) {
            updateQuery.set(root.get("originPrice"), bookVariant.getOriginPrice().get());
        }

        if (bookVariant.getSalePrice().isPresent()) {
            updateQuery.set(root.get("salePrice"), bookVariant.getSalePrice().get());
        }

        if (bookVariant.getIsbn().isPresent()) {
            updateQuery.set(root.get("isbn"), bookVariant.getIsbn().get());
        }

        updateQuery.where(builder.equal(root.get("id"), id));
        entityManager.createQuery(updateQuery).executeUpdate();
    }
}
