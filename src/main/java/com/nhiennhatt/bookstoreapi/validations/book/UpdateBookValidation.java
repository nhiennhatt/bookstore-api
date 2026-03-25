package com.nhiennhatt.bookstoreapi.validations.book;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.UUID;

@Getter
@Setter
public class UpdateBookValidation {
    @Length(min = 3, max = 255)
    private JsonNullable<String> name = JsonNullable.undefined();

    private JsonNullable<String> author = JsonNullable.undefined();

    private JsonNullable<String> description = JsonNullable.undefined();

    private JsonNullable<String> publisher = JsonNullable.undefined();

    private JsonNullable<String> distributor = JsonNullable.undefined();

    @Pattern(regexp = "ACTIVE|INACTIVE|COMING_SOON|DISCONTINUED")
    private JsonNullable<String> status = JsonNullable.undefined();

    public JsonNullable<BookStatus> getStatus() {
        if (status.isPresent()) {
            if (status.get() == null) return JsonNullable.of(null);
            return JsonNullable.of(BookStatus.valueOf(status.get()));
        }
        return JsonNullable.undefined();
    }
}
