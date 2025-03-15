package com.zestindiait.serviceImpl;
import com.zestindiait.entity.Order;
import com.zestindiait.customeExceptions.InvalidOrderException;
import com.zestindiait.customeExceptions.OrderNotFoundException;
import com.zestindiait.repository.OrderRepository;
import com.zestindiait.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        if (order == null || order.getProduct() == null || order.getQuantity() <= 0) {
            throw new InvalidOrderException("Invalid order data provided.");
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        return optionalOrder.get();
    }

    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = getOrderById(id);
        existingOrder.setProduct(updatedOrder.getProduct());
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }
}
