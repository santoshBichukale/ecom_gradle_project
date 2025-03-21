package com.zestindiait.controller;

import com.zestindiait.entity.Category;
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

import java.util.List;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CategoryController categoryController;

    @LocalServerPort
    private int port;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    private String tokan;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        tokan = jwtUtils.generateToken("admin");
        baseUrl = "http://localhost:" + port + "/api/categories";
    }

    @Test
    void createCategory() {
        headers.setBearerAuth(tokan);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Category request = Category.builder()
                .name("food")
                .build();
        HttpEntity<Category> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Category> response = restTemplate.postForEntity(baseUrl, entity, Category.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCategoryById() {
        headers.setBearerAuth(tokan);
        HttpEntity entity=new HttpEntity(headers);

        ResponseEntity<Category> response=restTemplate.exchange(baseUrl+"/1",HttpMethod.GET,entity,Category.class);

        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void getAllCategories() {
        headers.setBearerAuth(tokan);
        HttpEntity entity=new HttpEntity(headers);

        ResponseEntity<Category[]> response=restTemplate.exchange(baseUrl,
                HttpMethod.GET,
                entity,
                Category[].class);

        List<Category> categories = List.of(response.getBody());
        System.err.println(categories);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void updateCategory() {
        Category reqCategory=Category.builder().name("clothing").build();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokan);
        HttpEntity entity=new HttpEntity(reqCategory,headers);

       ResponseEntity<Category> response=restTemplate.exchange(baseUrl+"/3",HttpMethod.PUT,entity,Category.class);

       Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

//    @Test
//    void deleteCategory() {
//        headers.setBearerAuth(tokan);
//        HttpEntity entity=new HttpEntity(headers);
//
//        ResponseEntity <String> response=restTemplate.exchange(baseUrl+"/4",HttpMethod.DELETE,entity,String.class);
//
//
//        Assertions.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
//
//    }
}