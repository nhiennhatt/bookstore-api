package com.nhiennhatt.bookstoreapi.validations.category;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadCategoryImage {
    private MultipartFile file;
}
