package com.zestindiait.serviceImpl;

import com.zestindiait.customeExceptions.InvalidProductException;
import com.zestindiait.customeExceptions.ProductNotFoundException;
import com.zestindiait.entity.Category;
import com.zestindiait.entity.Product;
import com.zestindiait.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(1L, "Electronics");
        product = new Product(1L, "Laptop", "Gaming Laptop", 1200.00, category);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_NullProduct() {
        InvalidProductException exception = assertThrows(InvalidProductException.class, () -> {
            productService.createProduct(null);
        });

        assertEquals("Product cannot be null", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(product, new Product(2L, "Phone", "Smartphone", 800.00, product.getCategory()));
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Laptop", foundProduct.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Product not found with ID: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product(1L, "Updated Laptop", "Updated Description", 1500.00, product.getCategory());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Laptop", result.getName());
        assertEquals(1500.00, result.getPrice());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product updatedProduct = new Product(1L, "Updated Laptop", "Updated Description", 1500.00, product.getCategory());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, updatedProduct);
        });

        assertEquals("Product not found with ID: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NullProduct() {
        InvalidProductException exception = assertThrows(InvalidProductException.class, () -> {
            productService.updateProduct(1L, null);
        });

        assertEquals("Product cannot be null", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product not found with ID: 1", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
