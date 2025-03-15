package com.zestindiait.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zestindiait.dto.LoginRequest;
import com.zestindiait.dto.RegisterRequest;
import com.zestindiait.entity.Role;
import com.zestindiait.entity.User;
import com.zestindiait.customeExceptions.InvalidCredentialsException;
import com.zestindiait.customeExceptions.UserAlreadyExistsException;
import com.zestindiait.customeExceptions.UserNotFoundException;
import com.zestindiait.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user5", "encodedPassword", Role.USER);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        when(userRepository.findByUsername("user5")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("user5");

        assertNotNull(userDetails);
        assertEquals("user5", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("unknown"));
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest("newUser", "password", Role.USER);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(request);

        assertNotNull(registeredUser);
        assertEquals("user5", registeredUser.getUsername());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("user5", "password", Role.USER);
        when(userRepository.findByUsername("user5")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    void testLoginUser_Success() {
        LoginRequest request = new LoginRequest("user5", "password");
        when(userRepository.findByUsername("user5")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User loggedInUser = userService.loginUser(request);

        assertNotNull(loggedInUser);
        assertEquals("user5", loggedInUser.getUsername());
    }

    @Test
    void testLoginUser_UserNotFound() {
        LoginRequest request = new LoginRequest("unknown", "password");
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loginUser(request));
    }

    @Test
    void testLoginUser_InvalidPassword() {
        LoginRequest request = new LoginRequest("user5", "wrongPassword");
        when(userRepository.findByUsername("user5")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(request));
    }

    @Test
    void testFindByUsername_UserExists() {
        when(userRepository.findByUsername("user5")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("user5");

        assertTrue(foundUser.isPresent());
        assertEquals("user5", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("unknown");

        assertFalse(foundUser.isPresent());
    }
}
