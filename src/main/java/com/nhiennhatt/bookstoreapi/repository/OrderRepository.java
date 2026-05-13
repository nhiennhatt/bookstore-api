package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto;
import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomOrderRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, CustomOrderRepository {
    @Query("""
    SELECT new com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto(
        ord.id,
        new com.nhiennhatt.bookstoreapi.dto.user.MeResponse(u),
        a,
        ord.status,
        ord.subtotalPrice,
        ord.shippingFee,
        ord.orderDiscount,
        ord.shippingDiscount,
        ord.grandTotal,
        ord.createdAt
    )
    FROM (
        SELECT
            o.id as id,
            o.status as status,
            o.subtotalPrice as subtotalPrice,
            o.shippingFee as shippingFee,
            o.orderDiscount as orderDiscount,
            o.shippingDiscount as shippingDiscount,
            o.grandTotal as grandTotal,
            o.address.id as addressId,
            o.user.id as userId,
            o.createdAt as createdAt
        FROM Order o
        WHERE o.user.id = :userId
        ORDER BY createdAt DESC
    ) ord
    LEFT JOIN User u ON ord.userId = u.id
    LEFT JOIN UserAddress a ON ord.addressId = a.id
""")
    List<OrderOverviewDto> findOrderOverviewsByUser(@Param("userId") UUID userId);

    Order findOrderById(UUID id);

    @EntityGraph(attributePaths = {"user", "address"})
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Order findOrderByIdWithRelated(UUID id);
}
