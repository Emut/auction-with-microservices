package com.example.auctionservice.dto;

public class KafkaDto {
    private boolean isRequest;
    private Long id;
    private String payload;
    private String operation;
    private String responseToTopic;
    private String history;

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResponseToTopic() {
        return responseToTopic;
    }

    public void setResponseToTopic(String responseToTopic) {
        this.responseToTopic = responseToTopic;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}

