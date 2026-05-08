package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.models.BookCollection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface CustomCollectionRepository {
    List<BookCollection> paginate(int page, int size, Boolean isPublic, String keyword);
    boolean updatePriority(UUID id, int priority);
    void deleteCollection(UUID id);
    int updateCollection(UUID id, String name, boolean isPublic);
}
