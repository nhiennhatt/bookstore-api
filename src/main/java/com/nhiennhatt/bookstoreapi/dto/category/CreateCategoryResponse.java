package com.nhiennhatt.bookstoreapi.dto.category;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CreateCategoryResponse {
    private UUID id;
    private String slug;
}
