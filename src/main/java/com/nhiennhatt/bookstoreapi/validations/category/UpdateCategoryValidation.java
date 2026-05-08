package com.nhiennhatt.bookstoreapi.validations.category;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UpdateCategoryValidation {
    private JsonNullable<String> name = JsonNullable.undefined();
    private JsonNullable<Boolean> isPublic = JsonNullable.undefined();
    private JsonNullable<Boolean> isFeatured = JsonNullable.undefined();

    @Size(min = 8, max = 80)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
    private JsonNullable<String> slug = JsonNullable.undefined();

    public void setName(String name) {
        this.name = JsonNullable.of(name);
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = JsonNullable.of(isPublic);
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = JsonNullable.of(isFeatured);
    }
}
