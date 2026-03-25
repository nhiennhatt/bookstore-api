package com.nhiennhatt.bookstoreapi.validations.bookVariant;

import com.nhiennhatt.bookstoreapi.common.enums.BookVariantStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
public class CreateBookVariant {
    @NotNull
    private UUID bookId;

    @NotBlank
    private String name;

    @NotNull
    @Length(min = 8, max = 13)
    private String isbn;

    @Min(value = 1)
    private int originPrice;

    @Min(value = 1)
    private int salePrice;

    private int inventory;

    @NotNull
    @Pattern(regexp = "ACTIVE|INACTIVE")
    private String status;

    public BookVariantStatus getStatus() {
        return BookVariantStatus.valueOf(status);
    }
}
