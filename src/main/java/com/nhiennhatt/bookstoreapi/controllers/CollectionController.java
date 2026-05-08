package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import com.nhiennhatt.bookstoreapi.dto.bookcollections.CollectionBookOverviewDto;
import com.nhiennhatt.bookstoreapi.models.BookCollection;
import com.nhiennhatt.bookstoreapi.models.CollectionBook;
import com.nhiennhatt.bookstoreapi.services.BookCollectionService;
import com.nhiennhatt.bookstoreapi.services.CollectionBookService;
import com.nhiennhatt.bookstoreapi.services.MinioService;
import com.nhiennhatt.bookstoreapi.validations.bookCollection.*;
import com.nhiennhatt.bookstoreapi.validations.collectionBook.CreateCollectionBookValidation;
import com.nhiennhatt.bookstoreapi.validations.collectionBook.GetCollectionBookValidation;
import com.nhiennhatt.bookstoreapi.validations.collectionBook.UpdateCollectionBookValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collections")
@Tag(name = "Collections", description = "The Collections API")
public class CollectionController {
    @Autowired
    private BookCollectionService bookCollectionService;
    @Autowired
    private CollectionBookService collectionBookService;
    @Autowired
    private MinioService minioService;

    @PostMapping("/")
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<BookCollection> createCollection(@Valid @RequestBody CreateBookCollectionValidation collection) {
        BookCollection instance = bookCollectionService.createBookCollection(collection);
        return ResponseEntity.status(201).body(instance);
    }

    @GetMapping("")
    @Operation(security = {
            @SecurityRequirement(name = "bearer-auth"),
            @SecurityRequirement(name = "")
    })
    public ResponseEntity<List<BookCollection>> getCollections(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(0) @Max(20) int limit,
            @AuthenticationPrincipal CurrentUser user
    ) {
        List<BookCollection> collections = bookCollectionService.getAllBookCollections(
                new QueryBookCollectionValidation(keyword, isPublic, page, limit),
                user
        );
        return ResponseEntity.ok(collections);
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PutMapping("/{id}/priority")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> updateCollectionPriority(@PathVariable("id") UUID id, @Valid @RequestBody UpdateCollectionPriorityValidation priority) {
        bookCollectionService.updateCollectionPriority(id, priority.getPriority());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> deleteCollection(@PathVariable("id") UUID id) {
        bookCollectionService.deleteCollection(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PutMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<BookCollection> updateCollection(@PathVariable("id") UUID id, @Valid @RequestBody UpdateBookCollectionValidation collection) {
        int result = bookCollectionService.updateCollection(id, collection.getName(), collection.isPublic());
        return result == 1 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PostMapping("/{id}/books")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<CollectionBook> addBookToCollection(@PathVariable("id") UUID id, @Valid @RequestBody CreateCollectionBookValidation payload) {
        CollectionBook collectionBook = collectionBookService.addBookToCollection(id, payload.getBookId());
        try {
            collectionBook.getBook().setImage(minioService.getPresignedUrl(collectionBook.getBook().getImage()));
        } catch (Exception e) {
            collectionBook.getBook().setImage(null);
        }
        return ResponseEntity.status(201).body(collectionBook);
    }

    @GetMapping("/{id}/books")
    @Operation(security = {
            @SecurityRequirement(name = "bearer-auth"),
            @SecurityRequirement(name = "")
    })
    public ResponseEntity<List<CollectionBookOverviewDto>> getBooksInCollection(
            @PathVariable("id") UUID id,
            @RequestParam(required = false) @Min(0) Integer cursor,
            @RequestParam(required = false) @Min(10) @Max(30) Integer limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BookStatus bookStatus,
            @RequestParam(required = false) BookVariantStatus bookVariantStatus,
            @RequestParam(required = false) Boolean isStockValid,
            @AuthenticationPrincipal CurrentUser user
            ) {
        List<CollectionBookOverviewDto> books = collectionBookService.getCollectionBooks(
                id,
                CollectionBookFilter.builder()
                        .positionCursor(cursor)
                        .limit(limit)
                        .keyword(keyword)
                        .bookStatus(bookStatus)
                        .variantStatus(bookVariantStatus)
                        .isStockValid(isStockValid)
                        .build(),
                user
        );
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PutMapping("/{id}/books/{bookId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> updateCollectionBookPosition(
            @PathVariable @Valid UUID id,
            @PathVariable @Valid UUID bookId,
            @Valid @RequestBody UpdateCollectionBookValidation payload
    ) {
        collectionBookService.updateCollectionPosition(id, bookId, payload.getPosition());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @DeleteMapping("/{id}/books/{bookId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> deleteCollectionBook(
            @PathVariable @Valid UUID id,
            @PathVariable @Valid UUID bookId
    ){
        collectionBookService.deleteCollectionBook(id, bookId);
        return ResponseEntity.noContent().build();
    }
}
