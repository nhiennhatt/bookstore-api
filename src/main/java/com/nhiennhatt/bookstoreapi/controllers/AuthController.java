package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.dto.user.CreateUserResponse;
import com.nhiennhatt.bookstoreapi.dto.user.LoginResponse;
import com.nhiennhatt.bookstoreapi.models.CustomUserDetails;
import com.nhiennhatt.bookstoreapi.services.AuthService;
import com.nhiennhatt.bookstoreapi.validations.user.CreateUserValidation;
import com.nhiennhatt.bookstoreapi.validations.user.LoginValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserValidation user) {
        CreateUserResponse response = authService.createUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginValidation loginValidation) {
        String token = authService.loginUser(loginValidation.getUsername(), loginValidation.getPassword());
        return ResponseEntity.ok(LoginResponse.builder().token(token).build());
    }
}
