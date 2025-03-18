package com.zestindiait.controller;

import com.zestindiait.dto.LoginRequest;
import com.zestindiait.dto.RegisterRequest;
import com.zestindiait.entity.Role;
import com.zestindiait.entity.User;
import com.zestindiait.security.JwtUtils;
import com.zestindiait.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest registerRequest = new RegisterRequest("john", "password", Role.USER);
        User mockUser = new User(1L, "john", "password", Role.USER);

        when(userService.registerUser(registerRequest)).thenReturn(mockUser);

        ResponseEntity<String> response = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("User registered successfully"));
    }

    @Test
    void testRegisterUser_InvalidRequest() {
        ResponseEntity<String> response = authController.registerUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request cannot be null or incomplete", response.getBody());
    }

    @Test
    void testLoginUser_Success() {
        LoginRequest loginRequest = new LoginRequest("john", "password");
        User mockUser = new User(1L, "john", "password", Role.USER);

        when(userService.loginUser(loginRequest)).thenReturn(mockUser);
        when(jwtUtils.generateToken("john")).thenReturn("mockToken");

        ResponseEntity<?> response = authController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", response.getBody());
    }

    @Test
    void testLoginUser_InvalidRequest() {
        ResponseEntity<?> response = authController.loginUser(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid login request", response.getBody());
    }

    @Test
    void testGetUserByUsername_Found() {
        User mockUser = new User(1L, "john", "password", Role.USER);
        when(userService.findByUsername("john")).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = authController.getUserByUsername("john");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userService.findByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<User> response = authController.getUserByUsername("unknown");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
