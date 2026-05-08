package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.dto.StringDto;
import com.nhiennhatt.bookstoreapi.dto.books.BookDetailDto;
import com.nhiennhatt.bookstoreapi.dto.books.BookOverviewDto;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.services.BookService;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.CreateBookValidation;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookCategory;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "The Books API")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<List<BookOverviewDto>> getBooks(
            @RequestParam(required = false) UUID cursor,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) BookStatus bookStatus,
            @RequestParam(required = false) BookVariantStatus variantStatus,
            @RequestParam(required = false) Boolean isStockValid,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CurrentUser user
            ) {
        return ResponseEntity.ok(bookService.getBookOverviews(
                BookFilter.builder()
                        .cursor(cursor)
                        .categoryId(categoryId)
                        .isStockValid(isStockValid)
                        .bookStatus(bookStatus)
                        .variantStatus(variantStatus)
                        .limit(limit != null && limit > 0 ? limit : 10)
                        .keyword(keyword)
                        .build(),
                user
        ));
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Book> createBook(@RequestBody @Valid CreateBookValidation bookValidation) {
        Book book = bookService.createBook(bookValidation);
        return ResponseEntity.status(201).body(book);
    }

    @PatchMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateBook(@PathVariable("id") UUID id, @RequestBody @Valid UpdateBookValidation bookValidation) {
        bookService.updateBook(id, bookValidation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<BookDetailDto> getBookById(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookService.getBook(id, user));
    }

    @GetMapping("/{slug}/slug")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<BookDetailDto> getBookBySlug(@PathVariable("slug") String slug, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookService.getBook(slug, user));
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/category")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateBookCategory(
            @PathVariable("id") UUID id, @RequestBody @Valid UpdateBookCategory bookCategory
    ) {
        bookService.updateBookCategory(id, bookCategory.getCategoryId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/category")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<Category> getBookCategory(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        Category category = bookService.getBookCategory(id, user);
        return ResponseEntity.ok(category);
    }

    @PutMapping(value = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            security = {@SecurityRequirement(name = "bearer-auth")}
    )
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<StringDto> updateBookImage(
            @PathVariable("id") UUID id,
            @RequestParam @NotNull MultipartFile file
    ) {
        String url = bookService.updateBookImage(id, file);
        return ResponseEntity.ok(StringDto.builder().result(url).build());
    }
}
