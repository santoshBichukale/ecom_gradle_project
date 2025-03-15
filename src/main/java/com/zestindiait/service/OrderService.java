package com.zestindiait.service;

import com.zestindiait.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}