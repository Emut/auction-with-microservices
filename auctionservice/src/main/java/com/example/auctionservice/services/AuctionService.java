package com.example.auctionservice.services;

import org.springframework.kafka.annotation.KafkaListener;

public class AuctionService {

    private static final String TOPIC_NAME = "auction";

    @KafkaListener(topics = TOPIC_NAME, groupId = TOPIC_NAME)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);

    }
}
