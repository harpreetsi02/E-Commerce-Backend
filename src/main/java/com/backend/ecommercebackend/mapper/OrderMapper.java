package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.response.OrderItemResponse;
import com.backend.ecommercebackend.dto.response.OrderResponse;
import com.backend.ecommercebackend.entity.Order;
import com.backend.ecommercebackend.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order){

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();

        response.setId(item.getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setPriceAtOrderTime(item.getPriceAtOrderTime());

        BigDecimal subtotal = item.getPriceAtOrderTime()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        response.setSubtotal(subtotal);

        return response;
    }
}
