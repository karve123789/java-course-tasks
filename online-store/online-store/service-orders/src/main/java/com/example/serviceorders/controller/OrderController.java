package com.example.serviceorders.controller;

import com.example.serviceorders.controller.dto.CreateOrderRequest;
import com.example.serviceorders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("Получен запрос на создание заказа: {}", request);
        orderService.createOrder(request);
        return ResponseEntity.ok("Запрос на создание заказа принят в обработку.");
    }
}