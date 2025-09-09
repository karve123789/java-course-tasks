package com.example.servicenotifications.service;

import com.example.dto.OrderShippedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @KafkaListener(topics = "sent_orders", groupId = "notifications-group", concurrency = "2")
    public void handleOrderShipped(OrderShippedEvent event) {
        log.info("Получено событие об отгрузке заказа: {}", event);

        try {

            Thread.sleep(500); // Небольшая задержка для имитации
            log.info("===============================================================");
            log.info("Уведомление отправлено пользователю {}:", event.getUserId());
            log.info("Ваш заказ с ID {} на сумму {} был успешно отправлен.", event.getOrderId(), event.getTotalPrice());
            log.info("===============================================================");

        } catch (InterruptedException e) {
            log.error("Ошибка при отправке уведомления для заказа {}: {}", event.getOrderId(), e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}