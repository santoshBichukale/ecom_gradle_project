package com.zestindiait.controller;

import com.zestindiait.dto.LoginRequest;
import com.zestindiait.dto.RegisterRequest;
import com.zestindiait.entity.Role;
import com.zestindiait.entity.User;
import com.zestindiait.security.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String token;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        token = jwtUtils.generateToken("test");
    }

    @Test
    void testRegisterUser() {
        RegisterRequest request = new RegisterRequest("admin4", "123", Role.ADMIN);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLoginUser() {
        LoginRequest request = new LoginRequest("test", "123");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void testGetUserByUsername() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                baseUrl + "/user/testuser",
                HttpMethod.GET,
                requestEntity,
                User.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testUpdateUser() {
        RegisterRequest updateRequest = new RegisterRequest("updateduser", "newpassword", Role.ADMIN);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/user/1",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                requestEntity,
                User[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<User> users = List.of(response.getBody());
        System.err.println(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.size() >= 2);
    }

    @Test
    void testDeleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(baseUrl + "/user/2", HttpMethod.DELETE, requestEntity, Void.class);

        ResponseEntity<User> response = restTemplate.exchange(
                baseUrl + "/user/2",
                HttpMethod.GET,
                requestEntity,
                User.class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
