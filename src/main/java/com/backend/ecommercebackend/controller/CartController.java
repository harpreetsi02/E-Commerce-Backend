package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.CartItemRequest;
import com.backend.ecommercebackend.dto.response.CartResponse;
import com.backend.ecommercebackend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long userId
    ) {

        CartResponse response =
                cartService.getCartByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request
    ) {
        CartResponse response =
                cartService.addItemToCart(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartItemId
    ) {
        CartResponse response =
                cartService.removeItemFromCart(userId, cartItemId);

        return ResponseEntity.ok(response);
    }
}
