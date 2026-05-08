package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.BookCollection;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomCollectionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<BookCollection, UUID>, CustomCollectionRepository {
    BookCollection findBookCollectionById(UUID id);
}
