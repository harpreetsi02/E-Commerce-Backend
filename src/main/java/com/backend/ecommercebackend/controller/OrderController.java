package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.response.OrderResponse;
import com.backend.ecommercebackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @PathVariable Long userId
    ) {
        OrderResponse response =
                orderService.placeOrder(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long userId,
            @PathVariable Long orderId
    ) {
        OrderResponse response =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @PathVariable Long userId
    ) {
        List<OrderResponse> responses =
                orderService.getOrdersByUserId(userId);

        return ResponseEntity.ok(responses);
    }
}
