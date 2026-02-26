package com.nhiennhatt.bookstoreapi.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AppExceptionResponse<T> {
    private int status;
    private String errorCode;
    private String title;
    private T detail;
}
