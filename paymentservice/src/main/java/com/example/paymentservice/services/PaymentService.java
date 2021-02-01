package com.example.paymentservice.services;

import com.example.paymentservice.dto.KafkaDto;
import com.example.paymentservice.dto.PaymentDto;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final String TOPIC_NAME = "payment";

    private static final String USER_RESPONSE_TOPIC_NAME = "user_response";

    private final Gson gson = new Gson();

    private final PaymentRepository paymentRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Constants constants;

    public PaymentService(PaymentRepository paymentRepository, KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = TOPIC_NAME)
    public void listenGroup(String message) {
        System.out.println("Received Message in group: " + message);
        KafkaDto kafkaDto = gson.fromJson(message, KafkaDto.class);
        if (kafkaDto.getOperation().equals("makePayment")) {
            makePayment(kafkaDto);
        } else if (kafkaDto.getOperation().equals("getPayments")) {
            getPayments(kafkaDto);
        }

    }

    private void makePayment(KafkaDto kafkaDto) {
        PaymentDto paymentDto = gson.fromJson(kafkaDto.getPayload(), PaymentDto.class);
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        if (paymentDto.isCard() && paymentDto.getCardNumber().length() < 10) {
            //dummmy decline
            kafkaDto.setRequest(false);
            kafkaDto.setErrorMessage("Card number is invalid!");
            kafkaTemplate.send(USER_RESPONSE_TOPIC_NAME, gson.toJson(kafkaDto));
            return;
        }
        Optional<Payment> paymentOptional = paymentRepository.findByItemIdAndIsAcceptedIsTrue(paymentDto.getItemId());
        if(paymentOptional.isPresent()){
            //payment already made
            kafkaDto.setRequest(false);
            kafkaDto.setErrorMessage("Item is already paid for!");
            kafkaTemplate.send(USER_RESPONSE_TOPIC_NAME, gson.toJson(kafkaDto));
            return;
        }

        paymentDto.setAccepted(true);
        Payment payment = paymentRepository.save(paymentDto.getPayment());
        kafkaDto.setRequest(false);
        kafkaDto.setPayload(gson.toJson(new PaymentDto(payment)));
        kafkaTemplate.send(USER_RESPONSE_TOPIC_NAME, gson.toJson(kafkaDto));
    }

    private void getPayments(KafkaDto kafkaDto) {
        kafkaDto.setHistory(kafkaDto.getHistory() + constants.getApplicationName() + "---");
        List<Payment> paymentList = paymentRepository.findAll();
        List<PaymentDto> paymentDtoList = paymentList.stream().map(PaymentDto::new).collect(Collectors.toList());
        kafkaDto.setPayload(gson.toJson(paymentDtoList));
        kafkaDto.setRequest(false);
        kafkaTemplate.send(USER_RESPONSE_TOPIC_NAME, gson.toJson(kafkaDto));
    }
}
