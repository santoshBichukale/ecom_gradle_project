package com.zestindiait.serviceImpl;

import com.zestindiait.entity.Order;
import com.zestindiait.customeExceptions.InvalidOrderException;
import com.zestindiait.customeExceptions.OrderNotFoundException;
import com.zestindiait.customeExceptions.ProductNotFoundException;
import com.zestindiait.entity.Product;
import com.zestindiait.repository.OrderRepository;
import com.zestindiait.repository.ProductRepository;
import com.zestindiait.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order createOrder(Order order) {
        if (order == null || order.getProduct() == null || order.getOrderQuantity() <= 0) {
            throw new InvalidOrderException("Invalid order data provided.");
        }


        Product product = productRepository.findById(order.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + order.getProduct().getId()));


        if (product.getQuantity() < order.getOrderQuantity()) {
            throw new InvalidOrderException("Insufficient product quantity. Available: " + product.getQuantity());
        }


        product.setQuantity(product.getQuantity() - order.getOrderQuantity());
        productRepository.save(product);


        double totalPrice = product.getPrice() * order.getOrderQuantity();
        order.setTotal_price(totalPrice);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = getOrderById(id);
        Product existingProduct = existingOrder.getProduct();


        if (!existingProduct.getId().equals(updatedOrder.getProduct().getId())) {
            Product newProduct = productRepository.findById(updatedOrder.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + updatedOrder.getProduct().getId()));


            existingProduct.setQuantity(existingProduct.getQuantity() + existingOrder.getOrderQuantity());
            productRepository.save(existingProduct);


            if (newProduct.getQuantity() < updatedOrder.getOrderQuantity()) {
                throw new InvalidOrderException("Insufficient quantity for new product.");
            }
            newProduct.setQuantity(newProduct.getQuantity() - updatedOrder.getOrderQuantity());
            productRepository.save(newProduct);

            existingOrder.setProduct(newProduct);
        } else {

            int quantityDifference = updatedOrder.getOrderQuantity() - existingOrder.getOrderQuantity();
            if (quantityDifference > 0 && existingProduct.getQuantity() < quantityDifference) {
                throw new InvalidOrderException("Insufficient product quantity.");
            }
            existingProduct.setQuantity(existingProduct.getQuantity() - quantityDifference);
            productRepository.save(existingProduct);
        }


        existingOrder.setOrderQuantity(updatedOrder.getOrderQuantity());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        existingOrder.setTotal_price(existingOrder.getProduct().getPrice() * updatedOrder.getOrderQuantity());

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        Product product = order.getProduct();


        product.setQuantity(product.getQuantity() + order.getOrderQuantity());
        productRepository.save(product);

        orderRepository.deleteById(id);
    }
}
