package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CartItemRequest;
import com.backend.ecommercebackend.dto.response.CartItemResponse;
import com.backend.ecommercebackend.dto.response.CartResponse;
import com.backend.ecommercebackend.entity.Cart;
import com.backend.ecommercebackend.entity.CartItem;
import com.backend.ecommercebackend.entity.Product;
import com.backend.ecommercebackend.entity.User;
import com.backend.ecommercebackend.exception.CartNotFoundException;
import com.backend.ecommercebackend.exception.ProductNotFoundException;
import com.backend.ecommercebackend.exception.UserNotFoundException;
import com.backend.ecommercebackend.mapper.CartMapper;
import com.backend.ecommercebackend.repository.CartRepository;
import com.backend.ecommercebackend.repository.ProductRepository;
import com.backend.ecommercebackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(Long userId){
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found for user: " + userId
                        )
                );

        return cartMapper.toResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request){

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + request.getProductId()
                        )
                );

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Transactional
    public CartResponse removeItemFromCart(Long userId, Long cartItemId){

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found for user: " + userId
                        )
                );

        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toResponse(savedCart);
    }

    private Cart createNewCart(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
}
