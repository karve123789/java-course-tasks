package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Покупатель с ID " + request.getCustomerId() + " не найден"));

        List<Product> products = productRepository.findAllById(request.getProductIds());
        if (products.size() != request.getProductIds().size()) {
            throw new RuntimeException("Один или несколько продуктов не найдены");
        }

        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setShippingAddress(request.getShippingAddress());
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");

        return orderRepository.save(order);
    }
}