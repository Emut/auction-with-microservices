package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.ItemDto;
import com.example.inventoryservice.dto.KafkaDto;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private final List<ItemDto> items = new ArrayList<>();
    private final Gson gson = new Gson();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Constants constants;
    private static final String TOPIC_NAME = "inventory";

    public InventoryService(KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = TOPIC_NAME)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);
        KafkaDto kafkaDto = gson.fromJson(message, KafkaDto.class);
        if (kafkaDto.isRequest()) {
            if (kafkaDto.getOperation().equals("addItem")) {
                addItem(gson.fromJson(kafkaDto.getPayload(), ItemDto.class));
            }
            if (kafkaDto.getOperation().equals("getItems")) {
                getItems(kafkaDto);
            }
        }

    }

    void addItem(ItemDto itemDto) {
        itemDto.setId((long) items.size());
        items.add(itemDto);
    }

    void getItems(KafkaDto kafkaDto) {
        kafkaDto.setRequest(false);
        kafkaDto.setPayload(gson.toJson(items));
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");

        kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
    }

}
