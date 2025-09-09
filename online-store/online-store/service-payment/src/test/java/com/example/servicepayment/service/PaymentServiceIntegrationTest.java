package com.example.servicepayment.service;

import com.example.dto.OrderCreatedEvent;
import com.example.dto.OrderPaidEvent;
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
        brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" },
        topics = { "new_orders", "payed_orders" }
)
@Import(PaymentServiceIntegrationTest.TestResultConsumer.class)
class PaymentServiceIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TestResultConsumer testResultConsumer;

    @Test
    @DisplayName("Должен получить OrderCreatedEvent, обработать и отправить OrderPaidEvent")
    void shouldConsumeCreateEventAndProducePaidEvent() throws InterruptedException {

        var orderId = UUID.randomUUID();
        var createdEvent = new OrderCreatedEvent(orderId, "user-for-test", new BigDecimal("100.00"));

        kafkaTemplate.send("new_orders", orderId.toString(), createdEvent);

        boolean messageReceived = testResultConsumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageReceived, "Сообщение не было получено в топике payed_orders");

        ConsumerRecord<String, OrderPaidEvent> receivedRecord = testResultConsumer.getReceivedRecord();
        assertNotNull(receivedRecord);

        OrderPaidEvent paidEvent = receivedRecord.value();
        assertEquals(orderId, paidEvent.getOrderId());
        assertEquals("user-for-test", paidEvent.getUserId());
        assertEquals(new BigDecimal("100.00"), paidEvent.getTotalPrice());
    }


    @Component
    public static class TestResultConsumer {
        private CountDownLatch latch = new CountDownLatch(1);
        private ConsumerRecord<String, OrderPaidEvent> receivedRecord;

        @KafkaListener(
                topics = "payed_orders",
                groupId = "test-group",
                autoStartup = "true",
                properties = {"spring.json.value.default.type=com.example.dto.OrderPaidEvent"}
        )
        public void receive(ConsumerRecord<String, OrderPaidEvent> record) {
            this.receivedRecord = record;
            latch.countDown();
        }

        public CountDownLatch getLatch() {
            return latch;
        }

        public ConsumerRecord<String, OrderPaidEvent> getReceivedRecord() {
            return receivedRecord;
        }
    }
}