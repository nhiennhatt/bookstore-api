package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.validations.order.OrderFilter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomOrderRepository {
    List<Order> getOrders(OrderFilter orderFilter);
}
