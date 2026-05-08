package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomBookVariantRepository;
import com.nhiennhatt.bookstoreapi.repository.projection.BookWithVariantForOrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookVariantRepository extends JpaRepository<BookVariant, UUID>, CustomBookVariantRepository {
    List<BookVariant> findByBookId(UUID bookId);
    BookVariant findBookVariantById(UUID id);
    List<BookVariant> findBookVariantsByBook(Book book);

    @Query(
            """
            select new com.nhiennhatt.bookstoreapi.repository.projection.BookWithVariantForOrderProjection(
                b.id, b.name, v.id, v.name, b.status, v.status, v.inventory, v.originPrice, v.salePrice, v.weight
            )
            from BookVariant v
            left join Book b on v.bookId = b.id
            where v.id in :variantIds
            """
    )
    List<BookWithVariantForOrderProjection> getBookWithVariantForOrder(@Param("variantIds") List<UUID> variantIds);

    @Modifying
    @Query("update BookVariant set image = :image where id = :id")
    void updateBookVariantImage(@Param("id") UUID id, @Param("image") String image);

    BookVariant getBookVariantById(UUID id);

    @Modifying
    @Query("update BookVariant set inventory = inventory + :inventory where id = :id")
    void addInventory(UUID id, int inventory);
}
