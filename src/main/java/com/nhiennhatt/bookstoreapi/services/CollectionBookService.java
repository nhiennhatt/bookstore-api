package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.dto.bookcollections.CollectionBookOverviewDto;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookCollection;
import com.nhiennhatt.bookstoreapi.models.CollectionBook;
import com.nhiennhatt.bookstoreapi.repository.BookRepository;
import com.nhiennhatt.bookstoreapi.repository.CollectionBookRepository;
import com.nhiennhatt.bookstoreapi.repository.CollectionRepository;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.CollectionBookFilter;
import com.nhiennhatt.bookstoreapi.validations.collectionBook.GetCollectionBookValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CollectionBookService {
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private CollectionBookRepository collectionBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MinioService minioService;

    @Transactional
    public CollectionBook addBookToCollection(UUID collectionId, UUID bookId) {
        Book book = bookRepository.getReferenceById(bookId);
        BookCollection collection = collectionRepository.getReferenceById(collectionId);
        long count = collectionBookRepository.countCollectionBooksByCollectionId(collectionId);
        CollectionBook collectionBook = new CollectionBook();
        collectionBook.setCollection(collection);
        collectionBook.setBook(book);
        collectionBook.setPosition(count + 1);
        collectionBookRepository.save(collectionBook);
        return collectionBook;
    }

    public List<CollectionBookOverviewDto> getCollectionBooks(UUID collectionId, CollectionBookFilter filter, CurrentUser user) {
        if (user == null || user.getRole() == UserRole.ROLE_CUSTOMER) {
            filter.setBookStatus(BookStatus.ACTIVE);
            filter.setVariantStatus(BookVariantStatus.ACTIVE);
            filter.setIsStockValid(true);
        }

        return collectionBookRepository
                .getCollectionBooksByCollectionId(collectionId, filter)
                .stream().peek(collectionBook -> {
                    try {
                        if (collectionBook.getBook().getImage() != null)
                            collectionBook.getBook().setImage(minioService.getPresignedUrl(collectionBook.getBook().getImage()));
                    } catch (Exception e) {
                        collectionBook.getBook().setImage(null);
                    }
                }).toList();
    }

    @Transactional
    public void updateCollectionPosition(UUID collectionId, UUID bookId, long position) {
        collectionBookRepository.updateCollectionBookPosition(collectionId, bookId, position);
    }

    @Transactional
    public void deleteCollectionBook(UUID collectionId, UUID bookId) {
        collectionBookRepository.deleteCollectionBook(collectionId, bookId);
    }
}
