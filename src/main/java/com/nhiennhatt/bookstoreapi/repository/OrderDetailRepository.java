package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.dto.orders.OrderDetailDto;
import com.nhiennhatt.bookstoreapi.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    @Query("""
    SELECT
        new com.nhiennhatt.bookstoreapi.dto.orders.OrderDetailDto(
            o.id, o.variantId, o.bookId, b.name, b.slug, o.variantName, o.quantity, o.originUnitPrice, o.unitPrice, o.totalPrice, o.image
        )
    FROM (
        SELECT
            o.id as id,
            o.quantity as quantity,
            o.unitPrice as unitPrice,
            o.originUnitPrice as originUnitPrice,
            o.bookVariantId as bookVariantId,
            o.totalPrice as totalPrice,
            v.id as variantId,
            v.name as variantName,
            v.bookId as bookId,
            v.image as image
        FROM (
                SELECT
                    o.id as id,
                    o.quantity as quantity,
                    o.unitPrice as unitPrice,
                    o.originUnitPrice as originUnitPrice,
                    o.totalPrice as totalPrice,
                    o.bookVariantId as bookVariantId
                FROM OrderDetail o
                WHERE o.orderId = :orderId
            ) o
        LEFT JOIN BookVariant v ON v.id = o.bookVariantId
    ) o
    LEFT JOIN Book b ON b.id = o.bookId
    """)
    List<OrderDetailDto> findOrderDetailsByOrderId(@Param("orderId") UUID orderId);
}
