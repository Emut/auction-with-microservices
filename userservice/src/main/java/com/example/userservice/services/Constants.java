package com.example.userservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Constants {

    @Value("${application.name}")
    String applicationName;

    @Value("${application.id}")
    Long applicationId;

    String getApplicationName() {
        return '{' + applicationName + applicationId + '}';
    }
}
