package com.zestindiait.repository;

import com.zestindiait.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setProduct("Laptop");
        order.setQuantity(2);
        order.setOrderDate("16/03/25");
    }

    @Test
    void testSaveOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getId());
        assertEquals("Laptop", savedOrder.getProduct());
        assertEquals(2, savedOrder.getQuantity());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderRepository.findById(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals("Laptop", foundOrder.get().getProduct());

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderRepository.findById(2L);

        assertFalse(foundOrder.isPresent());

        verify(orderRepository, times(1)).findById(2L);
    }

    @Test
    void testFindAllOrders() {
        Order order2 = new Order();
        order2.setId(2L);
        order2.setProduct("Phone");
        order2.setQuantity(1);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order, order2));

        List<Order> orders = orderRepository.findAll();

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderRepository.deleteById(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        assertTrue(orderRepository.existsById(1L));

        verify(orderRepository, times(1)).existsById(1L);
    }
}
