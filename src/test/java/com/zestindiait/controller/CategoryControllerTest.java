package com.zestindiait.controller;

import com.zestindiait.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/categories";
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Sports");

        ResponseEntity<Category> response = restTemplate.postForEntity(baseUrl, category, Category.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Sports", response.getBody().getName());
    }

    @Test
    void testGetCategoryById() {
        ResponseEntity<Category> response = restTemplate.getForEntity(baseUrl + "/1", Category.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void testGetAllCategories() {
        ResponseEntity<Category[]> response = restTemplate.getForEntity(baseUrl, Category[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Category> categories = List.of(response.getBody());
        Assertions.assertFalse(categories.isEmpty());
        Assertions.assertEquals(3, categories.size());
    }

    @Test
    void testUpdateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Food");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Category> requestEntity = new HttpEntity<>(updatedCategory, headers);

        ResponseEntity<Category> response = restTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, requestEntity, Category.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Updated Food", response.getBody().getName());
    }

    @Test
    void testDeleteCategory() {
        restTemplate.delete(baseUrl + "/3");

        ResponseEntity<Category> response = restTemplate.getForEntity(baseUrl + "/3", Category.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
