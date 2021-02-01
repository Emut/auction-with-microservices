package com.example.userservice.services;

import com.example.userservice.dto.ItemDto;
import com.example.userservice.dto.KafkaDto;
import com.example.userservice.dto.PaymentDto;
import com.google.gson.Gson;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AuctionService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Constants constants;

    private final Gson gson = new Gson();

    private final static String TOPIC_NAME = "auction";

    private final static String RESPONSE_TO_TOPIC_NAME = "user_response";

    public AuctionService(KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    public void startAuction(boolean isStart) {
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(constants.getRandom().nextLong());
        if (isStart)
            kafkaDto.setOperation("startAuction");
        else
            kafkaDto.setOperation("stopAuction");
        kafkaDto.setRequest(true);
        kafkaDto.setResponseToTopic(RESPONSE_TO_TOPIC_NAME);
        kafkaDto.setHistory(constants.getApplicationName() + "---");

        kafkaTemplate.send(TOPIC_NAME, gson.toJson(kafkaDto));
    }

    public ItemDto placeBid(ItemDto itemDto) {
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(constants.getRandom().nextLong());
        kafkaDto.setOperation("placeBid");
        kafkaDto.setRequest(true);
        kafkaDto.setResponseToTopic(RESPONSE_TO_TOPIC_NAME);
        kafkaDto.setHistory(constants.getApplicationName() + "---");
        kafkaDto.setPayload(gson.toJson(itemDto));

        CompletableFuture<KafkaDto> kafkaDtoCompletableFuture = new CompletableFuture<>();
        constants.getResponseWaiters().put(kafkaDto.getId(), kafkaDtoCompletableFuture);
        kafkaTemplate.send(TOPIC_NAME, gson.toJson(kafkaDto));
        KafkaDto response;
        try {
            response = kafkaDtoCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        if(response.getErrorMessage() != null) {
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        return gson.fromJson(response.getPayload(), ItemDto.class);
    }
}
