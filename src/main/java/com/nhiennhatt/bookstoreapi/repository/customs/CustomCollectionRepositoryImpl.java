package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.BookCollection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomCollectionRepositoryImpl implements CustomCollectionRepository {
    @Autowired
    private EntityManager entityManager;

    public List<BookCollection> paginate(int page, int size, Boolean isPublic, String keyword) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookCollection> query = builder.createQuery(BookCollection.class);
        Root<BookCollection> root = query.from(BookCollection.class);
        List<Predicate> predicates = new ArrayList<>();

        if (isPublic != null) {
            predicates.add(builder.equal(root.get("isPublic"), isPublic));
        }

        if (keyword != null && !keyword.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }

        query.orderBy(builder.asc(root.get("priority")));

        TypedQuery<BookCollection> typedQuery = entityManager.createQuery(query.where(builder.and(predicates.toArray(new Predicate[0]))));
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public boolean updatePriority(UUID id, int priority) {
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

        BookCollection collection = entityManager.find(BookCollection.class, id);
        if (collection == null || collection.getPriority() == priority) return false;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<BookCollection> countRoot = countQuery.from(BookCollection.class);
        countQuery.select(builder.count(countRoot));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        if (priority > count) {
            String jpql = "UPDATE BookCollection c SET c.priority = c.priority - 1 WHERE c.priority >= :priority";
            entityManager.createQuery(jpql)
                    .setParameter("priority", priority)
                    .executeUpdate();
        } else if (priority > collection.getPriority()) {
            String jpql = "UPDATE BookCollection c SET c.priority = c.priority - 1 WHERE c.priority > :currentPriority AND c.priority <= :priority";
            entityManager.createQuery(jpql)
                    .setParameter("currentPriority", collection.getPriority())
                    .setParameter("priority", priority)
                    .executeUpdate();
        } else {
            String jpql = "UPDATE BookCollection c SET c.priority = c.priority + 1 WHERE c.priority < :currentPriority AND c.priority >= :priority";
            entityManager.createQuery(jpql)
                    .setParameter("currentPriority", collection.getPriority())
                    .setParameter("priority", priority)
                    .executeUpdate();
        }

        CriteriaUpdate<BookCollection> updateQuery = builder.createCriteriaUpdate(BookCollection.class);
        updateQuery.set("priority", priority);
        updateQuery.where(builder.equal(updateQuery.from(BookCollection.class).get("id"), id));
        entityManager.createQuery(updateQuery).executeUpdate();

        return true;
    }

    @Override
    public void deleteCollection(UUID id) {
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
        BookCollection collection = entityManager.find(BookCollection.class, id);
        if (collection == null) return;
        int priority = collection.getPriority();

        entityManager.remove(collection);

        String jpql = "UPDATE BookCollection c SET c.priority = c.priority - 1 WHERE c.priority > :priority";
        entityManager.createQuery(jpql)
                .setParameter("priority", priority)
                .executeUpdate();
    }

    @Override
    public int updateCollection(UUID id, String name, boolean isPublic) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<BookCollection> updateQuery = builder.createCriteriaUpdate(BookCollection.class);
        Root<BookCollection> root = updateQuery.from(BookCollection.class);
        updateQuery.set(root.get("name"), name);
        updateQuery.set(root.get("isPublic"), isPublic);
        updateQuery.where(builder.equal(root.get("id"), id));
        return entityManager.createQuery(updateQuery).executeUpdate();
    }
}
