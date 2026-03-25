package com.nhiennhatt.bookstoreapi.validations.book;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull
    @Pattern(regexp = "ACTIVE|INACTIVE|COMING_SOON|DISCONTINUED")
    private String status;

    public BookStatus getStatus() {
        return BookStatus.valueOf(status);
    }
}
