package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.user.CreateUserResponse;
import com.nhiennhatt.bookstoreapi.dto.user.LoginResponse;
import com.nhiennhatt.bookstoreapi.models.CustomUserDetails;
import com.nhiennhatt.bookstoreapi.services.AuthService;
import com.nhiennhatt.bookstoreapi.validations.user.ChangePasswordValidation;
import com.nhiennhatt.bookstoreapi.validations.user.CreateUserValidation;
import com.nhiennhatt.bookstoreapi.validations.user.GetTokenValidation;
import com.nhiennhatt.bookstoreapi.validations.user.LoginValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "The Auth API")
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
        LoginResponse res = authService.loginUser(loginValidation.getUsername(), loginValidation.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> getToken(@Valid @RequestBody GetTokenValidation body) {
        LoginResponse res = authService.refreshToken(body.getRefreshToken());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordValidation body, @AuthenticationPrincipal CurrentUser user) {
        authService.changePassword(body.getOldPassword(), body.getNewPassword(), user);
        return ResponseEntity.ok().build();
    }
}
