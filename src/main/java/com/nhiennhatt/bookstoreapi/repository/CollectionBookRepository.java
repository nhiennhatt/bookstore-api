package com.nhiennhatt.bookstoreapi.repository;

import com.nhiennhatt.bookstoreapi.models.CollectionBook;
import com.nhiennhatt.bookstoreapi.repository.customs.CustomCollectionBookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectionBookRepository extends JpaRepository<CollectionBook, UUID>, CustomCollectionBookRepository {
    long countCollectionBooksByCollectionId(UUID collectionId);
}
