package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.services.MeService;
import com.nhiennhatt.bookstoreapi.validations.address.CreateAddressValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Me", description = "The Me API")
public class MeController {
    @Autowired
    private MeService meService;

    @GetMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<MeResponse> getMe(@AuthenticationPrincipal CurrentUser user) {
        MeResponse response = meService.getMe(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addresses")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddress> addAddress(
            @AuthenticationPrincipal CurrentUser user,
            @RequestBody @Valid CreateAddressValidation address
    ) {
        UserAddress userAddress = meService.addAddress(user, address);
        return ResponseEntity.ok(userAddress);
    }

    @GetMapping("/addresses")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserAddress>> getAddresses(
            @AuthenticationPrincipal CurrentUser user
    ) {
        return ResponseEntity.ok(meService.getAddresses(user));
    }

    @GetMapping("/orders")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderOverviewDto>> getOrders(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(meService.getOrders(user));
    }
}
