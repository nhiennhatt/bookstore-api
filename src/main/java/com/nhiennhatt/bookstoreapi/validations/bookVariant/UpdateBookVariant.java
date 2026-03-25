package com.nhiennhatt.bookstoreapi.validations.bookVariant;

import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UpdateBookVariant {
    @NotBlank
    private JsonNullable<String> name = JsonNullable.undefined();

    @NotNull
    @Length(min = 8, max = 13)
    private JsonNullable<String> isbn = JsonNullable.undefined();

    @Min(value = 1)
    private JsonNullable<Integer> originPrice = JsonNullable.undefined();

    @Min(value = 1)
    private JsonNullable<Integer> salePrice = JsonNullable.undefined();

    private JsonNullable<Integer> inventory = JsonNullable.undefined();

    @Pattern(regexp = "ACTIVE|INACTIVE")
    private JsonNullable<String> status = JsonNullable.undefined();

    public JsonNullable<BookVariantStatus> getStatus() {
        if (status.isPresent()) {
            if (status.get() == null) return JsonNullable.of(null);
            return JsonNullable.of(BookVariantStatus.valueOf(status.get()));
        }
        return JsonNullable.undefined();
    }
}
