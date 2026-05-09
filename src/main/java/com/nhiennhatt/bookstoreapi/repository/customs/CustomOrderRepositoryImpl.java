package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.validations.order.OrderFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> getOrders(OrderFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> order = query.from(Order.class);
       order.fetch("address");

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCursor() != null) {
            predicates.add(cb.lessThan(order.get("id"), filter.getCursor()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(order.get("status"), filter.getStatus()));
        }

        if (filter.getDeliveryCode() != null) {
            predicates.add(cb.equal(order.get("deliveryCode"), filter.getDeliveryCode()));
        }

        if (filter.getPaymentCode() != null) {
            predicates.add(cb.equal(order.get("paymentCode"), filter.getPaymentCode()));
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            predicates.add(cb.between(order.get("createdAt"), filter.getStartDate(), filter.getEndDate()));
        } else if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(order.get("createdAt"), filter.getStartDate()));
        } else if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(order.get("createdAt"), filter.getEndDate()));
        }

        if (filter.getProvinceId() != null) {
            predicates.add(cb.equal(order.get("address").get("provinceId"), filter.getProvinceId()));
        }

        if (filter.getDistrictId() != null) {
            predicates.add(cb.equal(order.get("address").get("districtId"), filter.getDistrictId()));
        }

        if (filter.getWardCode() != null) {
            predicates.add(cb.equal(order.get("address").get("wardCode"), filter.getWardCode()));
        }

        query.select(order)
                .where(cb.and(predicates))
                .orderBy(cb.desc(order.get("id")));

        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(filter.getLimit());

        return typedQuery.getResultList();
    }
}
