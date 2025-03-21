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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private JwtUtils jwtUtils;


     RestTemplate restTemplate=new RestTemplate();

    @LocalServerPort
    private int port;

    private String token;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        token = jwtUtils.generateToken("admin");
    }

    @Test
    void testRegisterUser() {
        RegisterRequest request = new RegisterRequest("admin4", "123", Role.ADMIN);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", request, String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testLoginUser() {
        LoginRequest request = new LoginRequest("admin", "123");

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
                baseUrl + "/user/admin",
                HttpMethod.GET,
                requestEntity,
                User.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    void testUpdateUser() {
        RegisterRequest updateRequest = new RegisterRequest("updateduser", "newpassword", Role.ADMIN);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/user/3",
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

    }

    @Test
    void testDeleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                baseUrl + "/user/3", HttpMethod.DELETE, requestEntity, String.class);


        Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }

}