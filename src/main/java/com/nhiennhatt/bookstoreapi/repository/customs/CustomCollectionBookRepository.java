package com.nhiennhatt.bookstoreapi.repository.customs;

import com.nhiennhatt.bookstoreapi.dto.bookcollections.CollectionBookOverviewDto;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.CollectionBookFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface CustomCollectionBookRepository {
    List<CollectionBookOverviewDto> getCollectionBooksByCollectionId(
            @NotNull UUID collectionId, CollectionBookFilter filter
    );

    int updateCollectionBookPosition(UUID collectionId, UUID bookId, long position);

    int deleteCollectionBook(UUID collectionId, UUID bookId);
}
