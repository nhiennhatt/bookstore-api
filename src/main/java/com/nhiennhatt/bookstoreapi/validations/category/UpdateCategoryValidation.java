package com.nhiennhatt.bookstoreapi.validations.category;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
public class UpdateCategoryValidation {
    private JsonNullable<String> name = JsonNullable.undefined();
    private JsonNullable<Boolean> isPublic = JsonNullable.undefined();

    public void setName(String name) {
        this.name = JsonNullable.of(name);
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = JsonNullable.of(isPublic);
    }
}
