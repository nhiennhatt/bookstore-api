package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.repository.BookRepository;
import com.nhiennhatt.bookstoreapi.repository.BookVariantRepository;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.CreateBookVariant;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookVariantService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookVariantRepository bookVariantRepository;

    public BookVariant createBookVariant(UUID bookId, CreateBookVariant bookVariant) {
        BookVariant bookVariantEntity = BookVariant.builder()
                .book(bookRepository.getReferenceById(bookId))
                .name(bookVariant.getName())
                .isbn(bookVariant.getIsbn())
                .inventory(bookVariant.getInventory())
                .originPrice(bookVariant.getOriginPrice())
                .salePrice(bookVariant.getSalePrice())
                .status(bookVariant.getStatus())
                .bookId(bookId)
                .build();

        return bookVariantRepository.save(bookVariantEntity);
    }

    @Transactional
    public void updateVariant(UUID id, UpdateBookVariant bookVariant) {
        bookVariantRepository.partialUpdate(id, bookVariant);
    }

    public BookVariant getVariant(UUID id, CurrentUser user) {
        BookVariant bookVariant = bookVariantRepository.findBookVariantById(id);
        if (bookVariant != null && bookVariant.getStatus() == BookVariantStatus.INACTIVE && (user == null || user.getRole() == UserRole.ROLE_CUSTOMER))
            throw new AppException("Book is not available", "BOOK_NOT_AVAILABLE", 404, null, null);
        return bookVariant;
    }

    @Transactional
    public void deleteVariant(UUID id) {
        bookVariantRepository.deleteById(id);
    }

    public List<BookVariant> getBookVariants(UUID bookId) {
        Book book = bookRepository.getReferenceById(bookId);
        return bookVariantRepository.findBookVariantsByBook(book);
    }
}
