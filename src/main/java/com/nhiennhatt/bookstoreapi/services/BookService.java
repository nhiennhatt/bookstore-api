package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.dto.books.BookDetailDto;
import com.nhiennhatt.bookstoreapi.dto.books.BookOverviewDto;
import com.nhiennhatt.bookstoreapi.exceptions.AppException;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.repository.BookRepository;
import com.nhiennhatt.bookstoreapi.repository.CategoryRepository;
import com.nhiennhatt.bookstoreapi.utils.MimeTypeUtil;
import com.nhiennhatt.bookstoreapi.utils.Slugify;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.CreateBookValidation;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MinioService minioService;

    @Transactional
    public Book createBook(CreateBookValidation bookValidation) {
        String slug = bookValidation.getSlug() != null ? bookValidation.getSlug() : Slugify.slugify(bookValidation.getName(), 80);
        Category category = bookValidation.getCategoryId() != null ? categoryRepository.getReferenceById(bookValidation.getCategoryId()) : null;

        Book book = Book.builder()
                .name(bookValidation.getName())
                .description(bookValidation.getDescription())
                .author(bookValidation.getAuthor())
                .publisher(bookValidation.getPublisher())
                .distributor(bookValidation.getDistributor())
                .status(bookValidation.getStatus())
                .category(category)
                .categoryId(category != null ? bookValidation.getCategoryId() : null)
                .slug(slug)
                .build();

        bookRepository.save(book);
        return book;
    }

    @Transactional
    public void updateBook(UUID bookId, UpdateBookValidation bookValidation) {
        bookRepository.partialUpdate(bookId, bookValidation);
    }

    @Transactional
    public void deleteBook(UUID bookId) {
        boolean isBookExist = bookRepository.existsById(bookId);
        if (!isBookExist) throw new AppException("Book not found", "BOOK_NOT_FOUND", 404, null, null);
        bookRepository.deleteById(bookId);
        try {
            minioService.deletePattern(String.format("books/%s/", bookId.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateBookCategory(UUID id, UUID categoryId) {
        Category category = categoryRepository.getReferenceById(categoryId);
        if (category == null) throw new AppException("Category not found", "CATEGORY_NOT_FOUND", 404, null, null);
        bookRepository.updateCategory(id, category);
    }

    public BookDetailDto getBook(UUID id, CurrentUser user) {
        BookDetailDto book = bookRepository.getBookDetailDtoById(id);
        if (book == null || (book.getStatus() == BookStatus.INACTIVE && (user == null || user.getRole() == UserRole.ROLE_CUSTOMER)))
            throw new AppException("Book not found", "BOOK_NOT_FOUND", 404, null, null);
        if (book.getImage() != null) {
            try {
                book.setImage(minioService.getPresignedUrl(book.getImage()));
            } catch (Exception e) {
                book.setImage(null);
            }
        }
        return book;
    }

    public BookDetailDto getBook(String slug, CurrentUser user) {
        BookDetailDto book = bookRepository.getBookDetailDtoBySlug(slug);
        System.out.println(book == null);
        if (book == null || (book.getStatus() == BookStatus.INACTIVE && (user == null || user.getRole() == UserRole.ROLE_CUSTOMER)))
            throw new AppException("Book not found", "BOOK_NOT_FOUND", 404, null, null);
        if (book.getImage() != null) {
            try {
                book.setImage(minioService.getPresignedUrl(book.getImage()));
            } catch (Exception e) {
                book.setImage(null);
            }
        }
        return book;
    }

    public Category getBookCategory(UUID id, CurrentUser user) {
        Book book = bookRepository.getBookById(id);
        return book.getCategory();
    }

    public List<BookOverviewDto> getBookOverviews(BookFilter filter, CurrentUser user) {
        if (user == null || user.getRole() == UserRole.ROLE_CUSTOMER) {
            filter.setBookStatus(BookStatus.ACTIVE);
            filter.setVariantStatus(BookVariantStatus.ACTIVE);
            filter.setIsStockValid(true);
        }

        return bookRepository.getBookOverviews(filter).stream().peek(bookOverviewDto -> {
            try {
                if (bookOverviewDto.getImage() != null)
                    bookOverviewDto.setImage(minioService.getPresignedUrl(bookOverviewDto.getImage()));
            } catch (Exception e) {
                bookOverviewDto.setImage(null);
            }
        }).toList();
    }

    @Transactional
    public String updateBookImage(UUID id, MultipartFile file) {
        Book book = bookRepository.getBookById(id);
        if (book == null)
            throw new AppException("Book not found", "BOOK_NOT_FOUND", 404, null, null);

        try {
            if (book.getImage() != null) {
                minioService.deleteFile(book.getImage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            book.setImage(null);
            bookRepository.save(book);
        }

        try {
            String extension = MimeTypeUtil.getExtension(file.getInputStream());
            String fileName = String.format("books/%s/%s%s", book.getId(), book.getId(), extension);
            minioService.uploadFile(file, fileName);
            book.setImage(fileName);
            bookRepository.save(book);
            return minioService.getPresignedUrl(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
