package com.example.serviceshipping.service;

import com.example.dto.OrderPaidEvent;
import com.example.dto.OrderShippedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingService {

    private static final String TOPIC = "sent_orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group", concurrency = "3")
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("Получено событие об оплате заказа: {}", event);

        try {
            // Имитация процесса упаковки и отгрузки товара
            Thread.sleep(1500); // Имитируем задержку, чуть дольше чем оплата
            log.info("Заказ {} успешно упакован и отправлен.", event.getOrderId());
            OrderShippedEvent shippedEvent = new OrderShippedEvent(
                    event.getOrderId(),
                    event.getUserId(),
                    event.getTotalPrice()
            );

            kafkaTemplate.send(TOPIC, shippedEvent.getOrderId().toString(), shippedEvent);
            log.info("Отправлено событие об отгрузке заказа: {}", shippedEvent);

        } catch (InterruptedException e) {
            log.error("Ошибка при отгрузке заказа {}: {}", event.getOrderId(), e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}