package com.example.userservice.services;

import com.example.userservice.dto.ItemDto;
import com.example.userservice.dto.KafkaDto;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class InventoryService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Constants constants;

    public InventoryService(KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }


    private static final String TOPIC_NAME = "inventory";
    private static final String RESPONSE_TO_TOPIC_NAME = "user_response";
    private final Gson gson = new Gson();

    public List<ItemDto> getAllItems() {
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(constants.getRandom().nextLong());
        kafkaDto.setOperation("getItems");
        kafkaDto.setRequest(true);
        kafkaDto.setResponseToTopic(RESPONSE_TO_TOPIC_NAME);
        kafkaDto.setHistory(constants.getApplicationName() + "---");

        CompletableFuture<KafkaDto> kafkaDtoCompletableFuture = new CompletableFuture<>();
        constants.getResponseWaiters().put(kafkaDto.getId(), kafkaDtoCompletableFuture);
        kafkaTemplate.send(TOPIC_NAME, gson.toJson(kafkaDto));
        KafkaDto response;
        try {
            response = kafkaDtoCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return gson.fromJson(response.getPayload(), List.class);
    }

    public void addItem(ItemDto itemDto) {
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(1L);
        kafkaDto.setOperation("addItem");
        kafkaDto.setRequest(true);
        kafkaDto.setPayload(gson.toJson(itemDto));
        kafkaTemplate.send(TOPIC_NAME, gson.toJson(kafkaDto));
    }

    @KafkaListener(topics = RESPONSE_TO_TOPIC_NAME)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);
        KafkaDto kafkaDto = gson.fromJson(message, KafkaDto.class);
        if (kafkaDto.isRequest()) {
            return;
        }
        CompletableFuture<KafkaDto> completableFuture = constants.getResponseWaiters().get(kafkaDto.getId());
        if (completableFuture != null) {
            completableFuture.complete(kafkaDto);
        }
    }
}

