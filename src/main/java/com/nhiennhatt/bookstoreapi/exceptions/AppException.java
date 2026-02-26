package com.nhiennhatt.bookstoreapi.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private String code;
    private int status;
    private Object details;
    private String instance;

    public AppException(String message, String code, int status, Object details, String instance) {
        super(message);
        this.code = code;
        this.status = status;
        this.details = details;
        this.instance = instance;
    }
}
