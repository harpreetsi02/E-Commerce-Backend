package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.UserRequest;
import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    public User toEntity(UserRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());

        return user;
    }
}
