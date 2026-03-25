package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.services.BookVariantService;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.CreateBookVariant;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/variants")
public class BookVariantController {
    @Autowired
    private BookVariantService bookVariantService;

    @PostMapping("")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<BookVariant> createBookVariant(@RequestBody @Valid CreateBookVariant bookVariant) {
        BookVariant variant = bookVariantService.createBookVariant(bookVariant.getBookId(), bookVariant);
        return ResponseEntity.status(201).body(variant);
    }


    @GetMapping("")
    public ResponseEntity<List<BookVariant>> getAllVariants(@RequestParam("bookid") @Valid UUID bookId) {
        List<BookVariant> variants = bookVariantService.getBookVariants(bookId);
        return ResponseEntity.ok(variants);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public void updateVariant(@PathVariable("id") UUID id, @RequestBody @Valid UpdateBookVariant bookVariant) {
        bookVariantService.updateVariant(id, bookVariant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookVariant> getVariant(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookVariantService.getVariant(id, user));
    }

    @DeleteMapping("/{id}")
    public void deleteVariant(@PathVariable("id") UUID id) {
        bookVariantService.deleteVariant(id);
    }
}
