package com.example.servicenotifications.service;

import com.example.dto.OrderShippedEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"},
        topics = {"sent_orders", "notifications_orders_dlt"})
class NotificationServiceTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @SpyBean
    private NotificationService notificationService;

    private Consumer<String, String> dltConsumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("dlt-group-test", "true", embeddedKafkaBroker);
        dltConsumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        dltConsumer.subscribe(Collections.singletonList("notifications_orders_dlt"));
    }

    @AfterEach
    void tearDown() {
        dltConsumer.close();
    }

    @Test
    void testHandleOrderShipped_Success() {
        OrderShippedEvent event = new OrderShippedEvent(UUID.randomUUID(), "user-456", new BigDecimal("99.99"));

        kafkaTemplate.send("sent_orders", event);

        Mockito.verify(notificationService, timeout(5000).times(1))
                .handleOrderShipped(event);
    }

    @Test
    void testHandleOrderShipped_Failure_SendsToDLT() {
        UUID failedOrderId = UUID.randomUUID();
        OrderShippedEvent event = new OrderShippedEvent(failedOrderId, "user-fail", new BigDecimal("10.00"));

        doThrow(new RuntimeException("Симуляция ошибки отправки уведомления!"))
                .when(notificationService).handleOrderShipped(any(OrderShippedEvent.class));

        kafkaTemplate.send("sent_orders", event);

        Mockito.verify(notificationService, timeout(5000).times(3))
                .handleOrderShipped(any(OrderShippedEvent.class));

        ConsumerRecord<String, String> dltRecord = KafkaTestUtils.getSingleRecord(dltConsumer, "notifications_orders_dlt", Duration.ofMillis(5000));

        assertThat(dltRecord).isNotNull();
        assertThat(dltRecord.value()).contains("\"orderId\":\"" + failedOrderId.toString() + "\"");
        assertThat(dltRecord.value()).contains("\"userId\":\"user-fail\"");
    }
}