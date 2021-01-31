package com.example.auctionservice.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class StartupApplicationListener
        implements ServletContextListener {

    KafkaTemplate<String, String> kafkaTemplate;
    Constants constants;

    public StartupApplicationListener(KafkaTemplate<String, String> kafkaTemplate, Constants constants) {
        this.kafkaTemplate = kafkaTemplate;
        this.constants = constants;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        String message = constants.getApplicationName() + ',' + constants.serverPort + ',' + "OFF";
        kafkaTemplate.send("zookeeper", message);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Triggers when context initializes
        String message = constants.getApplicationName() + ',' + constants.serverPort + ',' + "ON";
        kafkaTemplate.send("zookeeper", message);
    }
}
