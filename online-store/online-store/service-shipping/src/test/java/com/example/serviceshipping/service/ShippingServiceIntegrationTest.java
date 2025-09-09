package com.example.serviceshipping.service;
import com.example.dto.OrderPaidEvent;
import com.example.dto.OrderShippedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9095", "port=9095" },
        topics = { "payed_orders", "sent_orders" }
)
@Import(ShippingServiceIntegrationTest.TestResultConsumer.class)
class ShippingServiceIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TestResultConsumer testResultConsumer;

    @Test
    @DisplayName("Должен получить OrderPaidEvent, обработать и отправить OrderShippedEvent")
    void shouldConsumePaidEventAndProduceShippedEvent() throws InterruptedException {

        var orderId = UUID.randomUUID();
        var paidEvent = new OrderPaidEvent(orderId, "user-shipping-test", new BigDecimal("250.50"));

        kafkaTemplate.send("payed_orders", orderId.toString(), paidEvent);

        boolean messageReceived = testResultConsumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageReceived, "Сообщение не было получено в топике sent_orders");

        ConsumerRecord<String, OrderShippedEvent> receivedRecord = testResultConsumer.getReceivedRecord();
        assertNotNull(receivedRecord);

        OrderShippedEvent shippedEvent = receivedRecord.value();
        assertEquals(orderId, shippedEvent.getOrderId());
        assertEquals("user-shipping-test", shippedEvent.getUserId());
        assertEquals(new BigDecimal("250.50"), shippedEvent.getTotalPrice());
    }


    @Component
    public static class TestResultConsumer {
        private final CountDownLatch latch = new CountDownLatch(1);
        private ConsumerRecord<String, OrderShippedEvent> receivedRecord;

        @KafkaListener(
                topics = "sent_orders",
                groupId = "test-shipping-group",
                autoStartup = "true",
                properties = {"spring.json.value.default.type=com.example.dto.OrderShippedEvent"}
        )
        public void receive(ConsumerRecord<String, OrderShippedEvent> record) {
            this.receivedRecord = record;
            latch.countDown();
        }

        public CountDownLatch getLatch() {
            return latch;
        }

        public ConsumerRecord<String, OrderShippedEvent> getReceivedRecord() {
            return receivedRecord;
        }
    }
}