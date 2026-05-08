package com.nhiennhatt.bookstoreapi.validations.address;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateAddressValidation {
    @NotBlank
    private String province;
    @Min(1)
    private int provinceId;
    @NotBlank
    private String district;
    @Min(1)
    private int districtId;
    @NotBlank
    private String ward;
    @NotBlank
    private String wardCode;
    @NotBlank
    private String address;
    @NotBlank
    @Pattern(regexp = "^[0-9]{9,12}$")
    private String phone;
    @NotBlank
    @Length(min = 3, max = 50)
    private String name;
}
