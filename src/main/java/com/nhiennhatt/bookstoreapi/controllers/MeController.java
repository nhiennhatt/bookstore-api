package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderOverviewDto;
import com.nhiennhatt.bookstoreapi.dto.user.MeResponse;
import com.nhiennhatt.bookstoreapi.models.UserAddress;
import com.nhiennhatt.bookstoreapi.services.MeService;
import com.nhiennhatt.bookstoreapi.validations.address.CreateAddressValidation;
import com.nhiennhatt.bookstoreapi.validations.user.UpdateUserInform;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PutMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<MeResponse> updateMe(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody UpdateUserInform userInform) {
        meService.updateUserInform(user.getId(), userInform);
        return ResponseEntity.ok(meService.getMe(user));
    }

    @PostMapping("/addresses")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<UserAddress> addAddress(
            @AuthenticationPrincipal CurrentUser user,
            @RequestBody @Valid CreateAddressValidation address
    ) {
        UserAddress userAddress = meService.addAddress(user, address);
        return ResponseEntity.ok(userAddress);
    }

    @GetMapping("/addresses")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<List<UserAddress>> getAddresses(
            @AuthenticationPrincipal CurrentUser user
    ) {
        return ResponseEntity.ok(meService.getAddresses(user));
    }

    @DeleteMapping("/addresses/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id, @AuthenticationPrincipal CurrentUser user) {
        meService.deleteAddress(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/addresses/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<UserAddress> updateAddress(
            @RequestBody @Valid CreateAddressValidation address,
            @PathVariable UUID id,
            @AuthenticationPrincipal CurrentUser user
    ) {
        UserAddress createdAddress = meService.updateAddress(id, user, address);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(createdAddress);
    }

    @PostMapping("/addresses/{id}/default")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<Void> setDefaultAddress(@PathVariable UUID id, @AuthenticationPrincipal CurrentUser user) {
        meService.setDefaultAddress(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    public ResponseEntity<List<OrderOverviewDto>> getOrders(@AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(meService.getOrders(user));
    }
}
