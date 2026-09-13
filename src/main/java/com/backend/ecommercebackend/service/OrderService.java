package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.OrderStatusUpdateRequest;
import com.backend.ecommercebackend.dto.response.OrderResponse;
import com.backend.ecommercebackend.entity.*;
import com.backend.ecommercebackend.exception.*;
import com.backend.ecommercebackend.mapper.OrderMapper;
import com.backend.ecommercebackend.repository.CartRepository;
import com.backend.ecommercebackend.repository.OrderRepository;
import com.backend.ecommercebackend.repository.ProductRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse placeOrder(Long userId, String idempotencyKey){

        Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (existingOrder.isPresent()){
            return orderMapper.toResponse(existingOrder.get());
        }

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found for user: " + userId
                        )
                );

        if (cart.getItems().isEmpty()){
            throw new CartEmptyException(
                    "Cannot place order with an empty cart"
            );
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Status.PENDING);
        order.setIdempotencyKey(idempotencyKey);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()){

            Product product = cartItem.getProduct();
            int requestedQty = cartItem.getQuantity();

            if (product.getStockQuantity() < requestedQty){
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName() +
                                ". Available: " + product.getStockQuantity() + ", Requested: " + requestedQty
                );
            }

            product.setStockQuantity(product.getStockQuantity() - requestedQty);
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQty);
            orderItem.setPriceAtOrderTime(product.getPrice());

            order.getItems().add(orderItem);

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(requestedQty));
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not fount with id: " + orderId
                        )
                );

        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId){

        return orderRepository.findByUserIdWithItems(userId)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );

        Status newStatus;

        try{
            newStatus = Status.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusTransitionException(
                    "Invalid status value: " + e.getMessage()
            );
        }

        if (!order.getStatus().canTransitionTo(newStatus)){
            throw new InvalidOrderStatusTransitionException(
                    "Cannot transition order from " + order.getStatus() + " to " + newStatus
            );
        }

        order.setStatus(newStatus);
        Order updateOrder = orderRepository.save(order);

        return orderMapper.toResponse(updateOrder);
    }
}
