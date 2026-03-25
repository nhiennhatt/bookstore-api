package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.models.Book;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.services.BookService;
import com.nhiennhatt.bookstoreapi.validations.book.BookFilter;
import com.nhiennhatt.bookstoreapi.validations.book.CreateBookValidation;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookCategory;
import com.nhiennhatt.bookstoreapi.validations.book.UpdateBookValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID cursor
    ) {
        return ResponseEntity.ok(bookService.getBooks(BookFilter.builder().categoryId(categoryId).cursor(cursor).build()));
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Book> createBook(@RequestBody @Valid CreateBookValidation bookValidation) {
        Book book = bookService.createBook(bookValidation);
        return ResponseEntity.status(201).body(book);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateBook(@PathVariable("id") UUID id, @RequestBody @Valid UpdateBookValidation bookValidation) {
        bookService.updateBook(id, bookValidation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookService.getBook(id, user));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Book> getBookBySlug(@PathVariable("slug") String slug, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookService.getBook(slug, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/category")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateBookCategory(
            @PathVariable("id") UUID id, @RequestBody @Valid UpdateBookCategory bookCategory
    ) {
        bookService.updateBookCategory(id, bookCategory.getCategoryId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/category")
    public ResponseEntity<Category> getBookCategory(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        Category category = bookService.getBookCategory(id, user);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateBookImage(@PathVariable("id") @Valid UUID id, @RequestParam @Valid MultipartFile file) {
        bookService.updateBookImage(id, file);
        return ResponseEntity.ok().build();
    }
}
