package com.nhiennhatt.bookstoreapi.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            AppException.class
    })
    public ResponseEntity<AppExceptionResponse> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(AppExceptionResponse.builder()
                        .status(ex.getStatus())
                        .errorCode(ex.getCode())
                        .title(ex.getMessage())
                        .detail(ex.getDetails())
                        .build()
                );
    }

    @ExceptionHandler({
            TokenExpiredException.class
    })
    public ResponseEntity<AppExceptionResponse> handleTokenExpiredException(TokenExpiredException ex) {
        return ResponseEntity.status(401)
                .body(AppExceptionResponse.builder()
                        .status(401)
                        .errorCode("TOKEN_EXPIRED")
                        .title("Token expired")
                        .build()
                );
    }

    @ExceptionHandler({
            JWTDecodeException.class,
            SignatureVerificationException.class
    })
    public ResponseEntity<AppExceptionResponse> handleJWTDecodeException(JWTDecodeException ex) {
        return ResponseEntity.status(401)
                .body(AppExceptionResponse.builder()
                        .status(401)
                        .errorCode("INVALID_TOKEN")
                        .title("Invalid token")
                        .build()
                );
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<AppExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(403)
                .body(AppExceptionResponse.builder()
                        .status(403)
                        .errorCode("ACCESS_DENIED")
                        .title("Access denied")
                        .build()
                );
    }

    @ExceptionHandler({
            AuthenticationException.class
    })
    public ResponseEntity<AppExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(401)
                .body(AppExceptionResponse.builder()
                        .status(401)
                        .errorCode("AUTHENTICATION_FAILED")
                        .title("Authentication failed")
                        .build()
                );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<AppExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(422)
                .body(AppExceptionResponse.builder()
                        .status(422)
                        .errorCode("VALIDATION_ERROR")
                        .title("Validation error")
                        .detail(errors)
                        .build()
                );
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    public ResponseEntity<AppExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
            if (constraintEx.getSQLState().equals("23505")) {
                Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\(");
                Matcher matcher = pattern.matcher(constraintEx.getMessage());
                if (matcher.find()) {
                    String columnName = matcher.group(1);
                    Map<String, String> params = Map.of("field", columnName);
                    return ResponseEntity.status(400)
                            .body(AppExceptionResponse.builder()
                                    .status(400)
                                    .errorCode("DUPLICATE_KEY")
                                    .title("Duplicate key")
                                    .detail(params)
                                    .build()
                            );
                }
            }
        }
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(AppExceptionResponse.builder()
                        .status(500)
                        .errorCode("INTERNAL_SERVER_ERROR")
                        .title("Internal server error")
                        .build()
                );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<AppExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400)
                .body(AppExceptionResponse.builder()
                        .status(400)
                        .errorCode("HTTP_MESSAGE_NOT_READABLE")
                        .title("HTTP message not readable")
                        .build()
                );
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<AppExceptionResponse> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(AppExceptionResponse.builder()
                        .status(500)
                        .errorCode("INTERNAL_SERVER_ERROR")
                        .title("Internal server error")
                        .build()
                );
    }
}
