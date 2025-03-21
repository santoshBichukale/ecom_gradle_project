package com.zestindiait.controller;

import com.zestindiait.entity.Category;
import com.zestindiait.entity.Product;
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
class ProductControllerTest {

    @Autowired
    ProductController productController;

    @Autowired
    JwtUtils jwtUtils;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;

    private String tokan;
    private String baseurl;

    @BeforeEach
    void setUp() {
        tokan = jwtUtils.generateToken("admin");
        baseurl = "http://localhost:" + port + "/api/products";
    }

    @Test
    void createProduct() {
        Product reqProduct = Product.builder()
                .name("notebook")
                .description("good quality blank pages")
                .price(25d)
                .quantity(100)
                .category(Category.builder().id(3L).build())
                .build();
        headers.setBearerAuth(tokan);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> entity = new HttpEntity<>(reqProduct, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseurl, entity, String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getAllProducts() {
        headers.setBearerAuth(tokan);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Product[]> response = restTemplate.exchange(baseurl + "/public", HttpMethod.GET, entity, Product[].class);

        Product[] products = response.getBody();
        System.err.println(products);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(products);
        Assertions.assertTrue(products.length > 0);
    }

    @Test
    void getProductById() {
        headers.setBearerAuth(tokan);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Product> response = restTemplate.exchange(baseurl + "/1", HttpMethod.GET, entity, Product.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Smartphone", response.getBody().getName());
    }

    @Test
    void updateProduct() {
        Product updatedProduct = Product.builder()
                .name("Updated Smartphone")
                .description("Updated model smartphone")
                .price(750d)
                .quantity(45)
                .category(Category.builder().id(1L).build())
                .build();

        headers.setBearerAuth(tokan);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> entity = new HttpEntity<>(updatedProduct, headers);

        ResponseEntity<Product> response = restTemplate.exchange(baseurl + "/1", HttpMethod.PUT, entity, Product.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated Smartphone", response.getBody().getName());
    }

    @Test
    void deleteProduct() {
        headers.setBearerAuth(tokan);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(baseurl + "/1", HttpMethod.DELETE, entity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Product deleted successfully", response.getBody());
    }
}
