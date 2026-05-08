package com.nhiennhatt.bookstoreapi.validations.book;

import com.nhiennhatt.bookstoreapi.common.enums.BookStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.UUID;

@Getter
@Setter
public class UpdateBookValidation {
    @Size(min = 3, max = 255)
    private JsonNullable<String> name = JsonNullable.undefined();

    private JsonNullable<String> author = JsonNullable.undefined();

    private JsonNullable<String> description = JsonNullable.undefined();

    private JsonNullable<String> publisher = JsonNullable.undefined();

    private JsonNullable<String> distributor = JsonNullable.undefined();

    @Size(min = 3, max = 80)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
    private JsonNullable<String> slug = JsonNullable.undefined();

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
