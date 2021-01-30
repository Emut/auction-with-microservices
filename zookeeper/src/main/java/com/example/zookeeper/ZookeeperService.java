package com.example.zookeeper;

import org.springframework.kafka.annotation.KafkaListener;

public class ZookeeperService {



    @KafkaListener(topics = "zookeeper")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group: " + message);


    }
}
