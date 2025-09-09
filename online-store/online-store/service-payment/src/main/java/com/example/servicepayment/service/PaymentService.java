package com.example.servicepayment.service;

import com.example.dto.OrderCreatedEvent;
import com.example.dto.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String TOPIC = "payed_orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "new_orders", groupId = "payment-group", concurrency = "3")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Получено событие о создании заказа: {}", event);

        try {
            Thread.sleep(1000); // Имитируем задержку для имитации
            log.info("Платеж для заказа {} успешно обработан.", event.getOrderId());
            OrderPaidEvent paidEvent = new OrderPaidEvent(
                    event.getOrderId(),
                    event.getUserId(),
                    event.getTotalPrice()
            );

            kafkaTemplate.send(TOPIC, paidEvent.getOrderId().toString(), paidEvent);
            log.info("Отправлено событие об оплате заказа: {}", paidEvent);

        } catch (InterruptedException e) {
            log.error("Ошибка при обработке платежа для заказа {}: {}", event.getOrderId(), e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}