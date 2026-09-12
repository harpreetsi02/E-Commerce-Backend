package com.backend.ecommercebackend.repository;

import com.backend.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
