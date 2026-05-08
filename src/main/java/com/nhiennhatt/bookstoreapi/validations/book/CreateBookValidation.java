package com.nhiennhatt.bookstoreapi.validations.book;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateBookValidation {
    @NotEmpty
    private String name;

    private String author;

    private String description;

    private String publisher;

    private String distributor;

    private UUID categoryId;

    @Size(min = 8, max = 80)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
    private String slug;

    @NotNull
    @Pattern(regexp = "ACTIVE|INACTIVE|COMING_SOON|DISCONTINUED")
    private String status;

    public BookStatus getStatus() {
        return BookStatus.valueOf(status);
    }
}
