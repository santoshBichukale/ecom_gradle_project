package com.zestindiait.service;

import com.zestindiait.dto.LoginRequest;
import com.zestindiait.dto.RegisterRequest;
import com.zestindiait.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User registerUser(RegisterRequest request);
    Optional<User> findByUsername(String username);
    User loginUser(LoginRequest request);
    User updateUser(Long id, RegisterRequest request);
    void deleteUser(Long id);

    List<User> getAllUsers();
}