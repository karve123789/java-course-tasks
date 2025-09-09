package com.example.serviceorders.service;

import com.example.dto.OrderCreatedEvent; // <-- Импорт из нашего kafka-common модуля
import com.example.serviceorders.controller.dto.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String TOPIC = "new_orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createOrder(CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();

        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                request.getUserId(),
                request.getTotalPrice()
        );

        kafkaTemplate.send(TOPIC, orderId.toString(), event);

        log.info("Отправлено событие о создании заказа: {}", event);
    }
}