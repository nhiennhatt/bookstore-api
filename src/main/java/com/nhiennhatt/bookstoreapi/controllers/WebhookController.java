package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/stripe")
    public ResponseEntity<Void> stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        orderService.verifyPayment(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}
