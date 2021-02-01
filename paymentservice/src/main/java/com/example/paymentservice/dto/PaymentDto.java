package com.example.paymentservice.dto;

import com.example.paymentservice.entity.Payment;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaymentDto {

    private Long id;

    private boolean isCard;

    private double price;

    private String cardNumber;

    private Long userId;

    private Long itemId;

    private boolean isAccepted;

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Payment getPayment(){
        Payment payment = new Payment();
        payment.setAccepted(isAccepted);
        payment.setCard(isCard);
        payment.setPrice(price);
        payment.setCardNumber(cardNumber);
        payment.setUserId(userId);
        payment.setItemId(itemId);
        payment.setId(id);
        return payment;
    }

    public PaymentDto(Payment payment) {
        id = payment.getId();
        userId = payment.getUserId();
        itemId = payment.getItemId();
        isCard = payment.isCard();
        isAccepted = payment.isAccepted();
        cardNumber = payment.getCardNumber();
        price = payment.getPrice();
    }
}

