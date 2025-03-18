package com.zestindiait.serviceImpl;
import com.zestindiait.customeExceptions.InvalidOrderException;
import com.zestindiait.customeExceptions.OrderNotFoundException;
import com.zestindiait.customeExceptions.ProductNotFoundException;
import com.zestindiait.entity.Order;
import com.zestindiait.entity.Product;
import com.zestindiait.repository.OrderRepository;
import com.zestindiait.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setQuantity(10);

        order = new Order();
        order.setId(1L);
        order.setProduct(product);
        order.setOrderQuantity(2);
        order.setOrderDate("2025-03-18");
    }

    @Test
    void createOrder_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getProduct().getId());


        verify(productRepository).save(any(Product.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_ProductNotFound_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(order));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_InsufficientStock_ThrowsException() {
        product.setQuantity(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(order));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }



    @Test
    void updateOrder_ProductNotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Order updatedOrder = new Order();
        updatedOrder.setProduct(new Product());
        updatedOrder.getProduct().setId(2L);

        assertThrows(ProductNotFoundException.class, () -> orderService.updateOrder(1L, updatedOrder));
    }

    @Test
    void updateOrder_InsufficientStock_ThrowsException() {
        Order updatedOrder = new Order();
        updatedOrder.setProduct(product);
        updatedOrder.setOrderQuantity(20);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderException.class, () -> orderService.updateOrder(1L, updatedOrder));
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));

        verify(orderRepository, never()).deleteById(anyLong());
    }
}
