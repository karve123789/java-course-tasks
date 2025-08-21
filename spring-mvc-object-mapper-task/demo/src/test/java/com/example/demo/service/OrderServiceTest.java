package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_Success() {

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);
        request.setProductIds(Arrays.asList(10L, 20L));
        request.setShippingAddress("Москва, ул. Тверская, 1");

        Customer customer = new Customer(1L, "Иван", "Иванов", "ivan@test.com", "123");
        Product product1 = new Product(10L, "Ноутбук", "", new BigDecimal("1000.00"), 5);
        Product product2 = new Product(20L, "Мышь", "", new BigDecimal("50.00"), 10);
        List<Product> products = Arrays.asList(product1, product2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(request.getProductIds())).thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.createOrder(request);

        assertNotNull(createdOrder);
        assertEquals("PENDING", createdOrder.getOrderStatus());
        assertEquals("Москва, ул. Тверская, 1", createdOrder.getShippingAddress());
        assertEquals(0, new BigDecimal("1050.00").compareTo(createdOrder.getTotalPrice()));
        assertEquals(2, createdOrder.getProducts().size());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_CustomerNotFound_ShouldThrowException() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request);
        });

        assertEquals("Покупатель с ID 99 не найден", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }
}