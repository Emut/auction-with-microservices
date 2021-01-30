package com.example.userservice.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("inventory", 6, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("payment", 1, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic("auction", 1, (short) 1);
    }

    @Bean
    public NewTopic topic4() {
        return new NewTopic("user", 1, (short) 1);
    }
    @Bean
    public NewTopic topic4_1() {
        return new NewTopic("user_response", 1, (short) 1);
    }

    @Bean
    public NewTopic topic5() {
        return new NewTopic("zookeeper", 1, (short) 1);
    }
}
