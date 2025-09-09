package com.example.serviceshipping;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
@Disabled
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        topics = {"payed_orders", "sent_orders"},
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9095",
                "port=9095"
        })
class ShippingServiceTest {

    @Test
    void testHandleOrderPaid_whenHappyPath_thenSendsToShippedOrdersTopic() {

    }
}