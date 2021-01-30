package com.example.inventoryservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Constants {

    @Value("${application.name}")
    String applicationName;

    @Value("${application.id}")
    Long applicationId;

    @Value("${server.port}")
    Integer serverPort;

    String getApplicationName() {
        return '{' + applicationName + applicationId + '}';
    }
}
