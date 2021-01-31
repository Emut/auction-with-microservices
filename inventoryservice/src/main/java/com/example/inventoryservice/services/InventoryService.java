package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.ItemDto;
import com.example.inventoryservice.dto.KafkaDto;
import com.example.inventoryservice.entity.Item;
import com.example.inventoryservice.repository.ItemRepository;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final Gson gson = new Gson();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Constants constants;

    private static final String TOPIC_NAME = "inventory";

    private final ItemRepository itemRepository;

    public InventoryService(KafkaTemplate<String, String> kafkaTemplate, Constants constants, ItemRepository itemRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
        this.itemRepository = itemRepository;
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
        itemRepository.save(itemDto.getItem());
    }

    void getItems(KafkaDto kafkaDto) {
        kafkaDto.setRequest(false);
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = items.stream().map(ItemDto::new).collect(Collectors.toList());
        kafkaDto.setPayload(gson.toJson(itemDtos));
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");

        kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
    }

}
