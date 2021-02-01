package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.ItemDto;
import com.example.inventoryservice.dto.KafkaDto;
import com.example.inventoryservice.dto.PaymentDto;
import com.example.inventoryservice.entity.Item;
import com.example.inventoryservice.repository.ItemRepository;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final Gson gson = new Gson();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Constants constants;

    private static final String TOPIC_NAME = "inventory";

    private static final String AUCTION_TOPIC_NAME = "auction";

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
            } else if (kafkaDto.getOperation().equals("getItems")) {
                getItems(kafkaDto);
            } else if (kafkaDto.getOperation().equals("makePayment")) {
                makePayment(kafkaDto);
            } if(kafkaDto.getOperation().equals("placeBid")){
                placeBid(kafkaDto);
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

    void makePayment(KafkaDto kafkaDto) {
        PaymentDto paymentDto = gson.fromJson(kafkaDto.getPayload(), PaymentDto.class);
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        Optional<Item> itemOptional = itemRepository.findById(paymentDto.getItemId());
        if (!itemOptional.isPresent()) {
            kafkaDto.setErrorMessage("Item not found!");
            kafkaDto.setRequest(false);
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
            return;
        }
        Item item = itemOptional.get();
        if (item.getUserId() == null || !item.getUserId().equals(paymentDto.getUserId())) {
            kafkaDto.setErrorMessage("Item is not yours!");
            kafkaDto.setRequest(false);
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
            return;
        }
        paymentDto.setPrice(item.getPrice());
        kafkaDto.setPayload(gson.toJson(paymentDto));
        kafkaTemplate.send(AUCTION_TOPIC_NAME, gson.toJson(kafkaDto));
    }

    void placeBid(KafkaDto kafkaDto) {
        ItemDto itemDto = gson.fromJson(kafkaDto.getPayload(), ItemDto.class);
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        kafkaDto.setRequest(false);
        Optional<Item> item = itemRepository.findById(itemDto.getId());
        //Note: this read, check, update is infact racy between inventoryServices.
        //This action can be done with a single UPDATE operation to be sync'ed by the DB.
        //But this is a PoC and you get the point right? :)
        //checks:
        if(!item.isPresent()){
            kafkaDto.setErrorMessage("Item does not exist!");
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
        }
        if(item.get().getPrice() >= itemDto.getPrice()){
            kafkaDto.setErrorMessage("Price can not be lower than:" + item.get().getPrice());
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
        }
        item.get().setUserId(itemDto.getUserId());
        item.get().setPrice(itemDto.getPrice());
        Item retval = itemRepository.save(item.get());
        kafkaDto.setPayload(gson.toJson(retval));
        kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
    }
}
