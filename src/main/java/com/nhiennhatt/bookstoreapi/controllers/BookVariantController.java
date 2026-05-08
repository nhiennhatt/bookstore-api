package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.StringDto;
import com.nhiennhatt.bookstoreapi.models.BookVariant;
import com.nhiennhatt.bookstoreapi.services.BookVariantService;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.CreateBookVariant;
import com.nhiennhatt.bookstoreapi.validations.bookVariant.UpdateBookVariant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/variants")
@Tag(name = "Book Variants", description = "The Book Variants API")
public class BookVariantController {
    @Autowired
    private BookVariantService bookVariantService;

    @PostMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<BookVariant> createBookVariant(@RequestBody @Valid CreateBookVariant bookVariant) {
        BookVariant variant = bookVariantService.createBookVariant(bookVariant.getBookId(), bookVariant);
        return ResponseEntity.status(201).body(variant);
    }


    @GetMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<List<BookVariant>> getAllVariants(@RequestParam("bookid") @Valid UUID bookId) {
        List<BookVariant> variants = bookVariantService.getBookVariants(bookId);
        return ResponseEntity.ok(variants);
    }

    @PatchMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public void updateVariant(@PathVariable("id") UUID id, @RequestBody @Valid UpdateBookVariant bookVariant) {
        bookVariantService.updateVariant(id, bookVariant);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<BookVariant> getVariant(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(bookVariantService.getVariant(id, user));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @DeleteMapping("/{id}")
    public void deleteVariant(@PathVariable("id") UUID id) {
        bookVariantService.deleteVariant(id);
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PutMapping(value = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<StringDto> updateVariantImage(@PathVariable("id") UUID id, @RequestParam MultipartFile file) {
        String result = bookVariantService.uploadImage(id, file);
        return ResponseEntity.ok(StringDto.builder().result(result).build());
    }
}
