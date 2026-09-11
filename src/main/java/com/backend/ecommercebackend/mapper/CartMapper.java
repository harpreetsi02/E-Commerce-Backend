package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.response.CartItemResponse;
import com.backend.ecommercebackend.dto.response.CartResponse;
import com.backend.ecommercebackend.entity.Cart;
import com.backend.ecommercebackend.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        response.setItems(itemResponses);

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(totalAmount);

        return response;
    }

    private CartItemResponse toItemResponse(CartItem item){

        CartItemResponse response = new CartItemResponse();

        response.setId(item.getId());
        response.setProductName(item.getProduct().getName());
        response.setProductPrice(item.getProduct().getPrice());
        response.setQuantity(item.getQuantity());

        BigDecimal subtotal = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        response.setSubtotal(subtotal);

        return response;
    }

}
