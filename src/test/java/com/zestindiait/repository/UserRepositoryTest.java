package com.zestindiait.repository;

import com.zestindiait.entity.Role;
import com.zestindiait.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByUsername("unknown");

        assertFalse(foundUser.isPresent());

        verify(userRepository, times(1)).findByUsername("unknown");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userRepository.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean exists = userRepository.existsById(1L);

        assertTrue(exists);

        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    void testExistsById_NotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        boolean exists = userRepository.existsById(99L);

        assertFalse(exists);

        verify(userRepository, times(1)).existsById(99L);
    }
}
