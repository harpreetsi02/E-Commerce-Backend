package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.OrderStatusUpdateRequest;
import com.backend.ecommercebackend.dto.response.OrderResponse;
import com.backend.ecommercebackend.service.OrderService;
import jakarta.validation.Valid;
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
            @PathVariable Long userId,
            @RequestHeader("Idempotency-Key") String idempotencyKey
    ) {
        OrderResponse response =
                orderService.placeOrder(userId, idempotencyKey);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
//            @PathVariable Long userId,
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

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request

    ) {
        OrderResponse response =
                orderService.updateOrderStatus(orderId, request);

        return ResponseEntity.ok(response);
    }
}
