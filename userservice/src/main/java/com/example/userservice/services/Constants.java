package com.example.userservice.services;

import com.example.userservice.dto.KafkaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class Constants {

    @Value("${application.name}")
    String applicationName;

    @Value("${application.id}")
    Long applicationId;

    @Value("${server.port}")
    Integer serverPort;

    String getApplicationName() {
        return '{' + applicationName + applicationId + '}';
    }

    private final Map<Long, CompletableFuture<KafkaDto>> responseWaiters = new HashMap<>();
    private final Random random = new Random();

    public Map<Long, CompletableFuture<KafkaDto>> getResponseWaiters() {
        return responseWaiters;
    }

    public Random getRandom() {
        return random;
    }
}
