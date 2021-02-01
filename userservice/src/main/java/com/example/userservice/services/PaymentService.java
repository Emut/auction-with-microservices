package com.example.userservice.services;

import com.example.userservice.dto.KafkaDto;
import com.example.userservice.dto.PaymentDto;
import com.google.gson.Gson;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Gson gson = new Gson();

    private final Constants constants;

    private final static String TOPIC_NAME = "inventory";

    private final static String PAYMENT_TOPIC_NAME = "payment";

    private static final String RESPONSE_TO_TOPIC_NAME = "user_response";

    public PaymentService(KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    public PaymentDto makePayment(PaymentDto paymentDto) {
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(constants.getRandom().nextLong());
        kafkaDto.setOperation("makePayment");
        kafkaDto.setRequest(true);
        kafkaDto.setResponseToTopic(RESPONSE_TO_TOPIC_NAME);
        kafkaDto.setHistory(constants.getApplicationName() + "---");
        kafkaDto.setPayload(gson.toJson(paymentDto));

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
        return gson.fromJson(response.getPayload(), PaymentDto.class);
    }

    public List<PaymentDto> getPayments(){
        KafkaDto kafkaDto = new KafkaDto();
        kafkaDto.setId(constants.getRandom().nextLong());
        kafkaDto.setOperation("getPayments");
        kafkaDto.setRequest(true);
        kafkaDto.setResponseToTopic(RESPONSE_TO_TOPIC_NAME);
        kafkaDto.setHistory(constants.getApplicationName() + "---");

        CompletableFuture<KafkaDto> kafkaDtoCompletableFuture = new CompletableFuture<>();
        constants.getResponseWaiters().put(kafkaDto.getId(), kafkaDtoCompletableFuture);
        kafkaTemplate.send(PAYMENT_TOPIC_NAME, gson.toJson(kafkaDto));
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
        return gson.fromJson(response.getPayload(), List.class);
    }
}
