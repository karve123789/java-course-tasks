package com.example.serviceorders.service;

import com.example.dto.OrderCreatedEvent;
import com.example.serviceorders.controller.dto.CreateOrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Должен сгенерировать событие и отправить его в Kafka")
    void createOrder_shouldGenerateEventAndSendToKafka() {
        var request = new CreateOrderRequest();
        request.setUserId("test-user-1");
        request.setTotalPrice(new BigDecimal("199.99"));
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        orderService.createOrder(request);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), eventCaptor.capture());

        String actualTopic = topicCaptor.getValue();
        String actualKey = keyCaptor.getValue();
        OrderCreatedEvent actualEvent = eventCaptor.getValue();

        assertEquals("new_orders", actualTopic);
        assertNotNull(actualEvent.getOrderId());
        assertEquals(actualKey, actualEvent.getOrderId().toString());
        assertEquals("test-user-1", actualEvent.getUserId());
        assertEquals(new BigDecimal("199.99"), actualEvent.getTotalPrice());
    }
}