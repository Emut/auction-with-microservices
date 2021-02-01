package com.example.auctionservice.services;

import com.example.auctionservice.dto.KafkaDto;
import com.example.auctionservice.entity.AuctionStatus;
import com.example.auctionservice.repository.AuctionStatusRepository;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuctionService {

    private static final String TOPIC_NAME = "auction";

    private static final String PAYMENT_TOPIC_NAME = "payment";

    private static final String ITEM_TOPIC_NAME = "inventory";

    private final Gson gson = new Gson();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Constants constants;

    private final AuctionStatusRepository auctionStatusRepository;

    public AuctionService(KafkaTemplate<String, String> kafkaTemplate, Constants constants, AuctionStatusRepository auctionStatusRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
        this.auctionStatusRepository = auctionStatusRepository;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = TOPIC_NAME)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);
        KafkaDto kafkaDto = gson.fromJson(message, KafkaDto.class);
        if (kafkaDto.isRequest()) {
            if (kafkaDto.getOperation().equals("makePayment")) {
                makePayment(kafkaDto);
            } else if (kafkaDto.getOperation().equals("startAuction")) {
                changeAuctionStatus(true);
            } else if (kafkaDto.getOperation().equals("stopAuction")) {
                changeAuctionStatus(false);
            } else if (kafkaDto.getOperation().equals("placeBid")){
                placeBid(kafkaDto);
            }
        }
    }

    private void makePayment(KafkaDto kafkaDto) {
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        Optional<AuctionStatus> auctionStatus = auctionStatusRepository.findDistinctTopByOrderByIdDesc();
        boolean isAuctionActive = false;
        if (auctionStatus.isPresent())
            isAuctionActive = auctionStatus.get().isActive();

        if (isAuctionActive) {
            kafkaDto.setErrorMessage("Auction is ongoing!");
            kafkaDto.setRequest(false);
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
            return;
        }

        kafkaTemplate.send(PAYMENT_TOPIC_NAME, gson.toJson(kafkaDto));
    }

    private void changeAuctionStatus(boolean isStart) {
        AuctionStatus auctionStatus = new AuctionStatus();
        if (isStart) {
            auctionStatus.setActive(true);
            auctionStatus.setStartDate(Instant.now());
        } else {
            auctionStatus.setActive(false);
            auctionStatus.setEndDate(Instant.now());
        }
        auctionStatusRepository.save(auctionStatus);
    }

    private void placeBid(KafkaDto kafkaDto){
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        Optional<AuctionStatus> auctionStatus = auctionStatusRepository.findDistinctTopByOrderByIdDesc();
        boolean isAuctionActive = false;
        if (auctionStatus.isPresent())
            isAuctionActive = auctionStatus.get().isActive();
        if(!isAuctionActive){
            // error
            kafkaDto.setRequest(false);
            kafkaDto.setErrorMessage("There is no ongoing auction!");
            kafkaTemplate.send(kafkaDto.getResponseToTopic(), gson.toJson(kafkaDto));
            return;
        }
        //hand over to inventory service for price check and insertion
        kafkaTemplate.send(ITEM_TOPIC_NAME, gson.toJson(kafkaDto));
    }
}
