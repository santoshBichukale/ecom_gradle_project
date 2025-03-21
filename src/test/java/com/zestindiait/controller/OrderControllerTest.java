package com.zestindiait.controller;

import com.zestindiait.entity.Order;
import com.zestindiait.entity.Product;
import com.zestindiait.entity.User;
import com.zestindiait.security.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private JwtUtils jwtUtils;

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    private String token;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        token = jwtUtils.generateToken("admin");
        baseUrl = "http://localhost:" + port + "/api/orders/";
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void createOrder() {
        Order order = new Order();
        order.setUser(User.builder().id(1L).build());
        order.setProduct(Product.builder().id(2L).build());
        order.setOrderQuantity(1);
        order.setTotal_price(699.99);

        HttpEntity<Order> entity = new HttpEntity<>(order, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Order registered successfully"));
    }

    @Test
    void getAllOrders() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Order[]> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, Order[].class);
        Order[] orders = response.getBody();
        System.err.println(orders);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    void getOrderById() {
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Order> response = restTemplate.exchange(baseUrl + "/2", HttpMethod.GET, entity, Order.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2L, response.getBody().getId());
    }


}