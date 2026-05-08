package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.orders.CreatePaymentIntentDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDetailDto;
import com.nhiennhatt.bookstoreapi.dto.orders.OrderDto;
import com.nhiennhatt.bookstoreapi.models.Order;
import com.nhiennhatt.bookstoreapi.services.OrderService;
import com.nhiennhatt.bookstoreapi.validations.order.CreateOrderValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "The Orders API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderValidation order, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.status(201).body(orderService.createOrder(order, user));
    }

    @PostMapping("/preview")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> previewOrder(@Valid @RequestBody CreateOrderValidation order, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(orderService.previewOrder(order, user));
    }

    @PostMapping("/{id}/payment-intent")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreatePaymentIntentDto> createPaymentIntent(@PathVariable() UUID id, @AuthenticationPrincipal CurrentUser user) {
        String clientSecret = orderService.createPaymentIntent(id, user);
        return ResponseEntity.status(201).body(new CreatePaymentIntentDto(clientSecret));
    }

    @GetMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-auth")},
            description = "Only allow ROLE_CONTENT_MANAGER, ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_INVENTORY_MANAGER, ROLE_PACKER, and the order owner to access this resource."
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable() UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(orderService.getOrderById(id, user));
    }
}
