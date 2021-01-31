package com.example.paymentservice.services;

import org.springframework.kafka.annotation.KafkaListener;

public class PaymentService {

    private static final String TOPIC_NAME = "payment";

    @KafkaListener(topics = TOPIC_NAME, groupId = TOPIC_NAME)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);

    }
}
