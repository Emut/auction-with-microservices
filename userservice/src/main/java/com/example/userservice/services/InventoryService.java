package com.example.userservice.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private KafkaTemplate<String, String> kafkaTemplate;

    public InventoryService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private final String TOPIC_NAME = "inventory";

    public void getAllItems(){
        kafkaTemplate.send(TOPIC_NAME, "Hello");
    }
}
