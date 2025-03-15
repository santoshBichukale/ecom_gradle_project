package com.zestindiait.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zestindiait.customeExceptions.InvalidOrderException;
import com.zestindiait.customeExceptions.OrderNotFoundException;
import com.zestindiait.entity.Order;
import com.zestindiait.entity.User;
import com.zestindiait.repository.OrderRepository;
import com.zestindiait.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = new User();
        user.setId(1L);

        sampleOrder = new Order(1L, user, "Product1", 5, "2024-03-16");
    }

    @Test
    void testCreateOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        Order createdOrder = orderService.createOrder(sampleOrder);

        assertNotNull(createdOrder);
        assertEquals("Product1", createdOrder.getProduct());
        verify(orderRepository, times(1)).save(sampleOrder);
    }

    @Test
    void testCreateOrder_InvalidOrder() {
        Order invalidOrder = new Order();

        assertThrows(InvalidOrderException.class, () -> orderService.createOrder(invalidOrder));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(sampleOrder));

        List<Order> orders = orderService.getAllOrders();

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrder_Success() {
        Order updatedOrder = new Order(1L, sampleOrder.getUser(), "UpdatedProduct", 10, "2024-03-17");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getProduct());
        assertEquals(10, result.getQuantity());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_NotFound() {
        Order updatedOrder = new Order(1L, sampleOrder.getUser(), "UpdatedProduct", 10, "2024-03-17");

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(1L, updatedOrder));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testDeleteOrder_Success() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, never()).deleteById(1L);
    }
}
