package com.nhiennhatt.bookstoreapi.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            DataIntegrityViolationException.class
    })
    public ResponseEntity<AppExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
            if (constraintEx.getSQLState().equals("23505")) {
                Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\(");
                Matcher matcher = pattern.matcher(constraintEx.getMessage());
                if (matcher.find()) {
                    String columnName = matcher.group(1);
                    Map<String, String> params = Map.of("column", columnName);
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
