package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.repository.BookRepository;
import com.nhiennhatt.bookstoreapi.repository.BookVariantRepository;
import com.nhiennhatt.bookstoreapi.utils.MimeTypeUtil;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.CreateBookVariant;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class BookVariantService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookVariantRepository bookVariantRepository;

    @Autowired
    private MinioService minioService;

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
        if (bookVariant == null) throw new AppException("Book variant not found", "BOOK_VARIANT_NOT_FOUND", 404, null, null);
        if (bookVariant.getStatus() == BookVariantStatus.INACTIVE && (user == null || user.getRole() == UserRole.ROLE_CUSTOMER))
            throw new AppException("Book is not available", "BOOK_NOT_AVAILABLE", 404, null, null);
        if (bookVariant.getImage() != null) {
            try {
                bookVariant.setImage(minioService.getPresignedUrl(bookVariant.getImage()));
            } catch (Exception e) {
                bookVariant.setImage(null);
            }
        }
        return bookVariant;
    }


    public List<BookVariant> getBookVariants(UUID bookId) {
        Book book = bookRepository.getReferenceById(bookId);
        return bookVariantRepository.findBookVariantsByBook(book).stream().peek(b -> {
            if (b.getImage() != null) {
                try {
                    b.setImage(minioService.getPresignedUrl(b.getImage()));
                } catch (Exception e) {
                    b.setImage(null);
                }
            }
        }).toList();
    }

    @Transactional
    public String uploadImage(UUID id, MultipartFile file) {
        BookVariant bookVariant = bookVariantRepository.getBookVariantById(id);
        if (bookVariant == null) throw new AppException("Book variant not found", "BOOK_VARIANT_NOT_FOUND", 404, null, null);
        try {
            String extension = MimeTypeUtil.getExtension(file.getInputStream());
            String fileName = String.format("books/%s/%s%s", bookVariant.getBookId(), bookVariant.getId(), extension);
            minioService.uploadFile(file, fileName);
            bookVariantRepository.updateBookVariantImage(bookVariant.getId(), fileName);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteVariant(UUID id) {
        BookVariant bookVariant = bookVariantRepository.findBookVariantById(id);
        if (bookVariant == null) throw new AppException("Book variant not found", "BOOK_VARIANT_NOT_FOUND", 404, null, null);
        try {
            minioService.deleteFile(bookVariant.getImage());
        } catch (Exception ignored) {}
        bookVariantRepository.deleteById(id);
    }
}
