package com.fintech.banking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    
    @Bean
    public NewTopic bankingEventsTopic() {
        return TopicBuilder.name("banking-events")
            .partitions(3)
            .replicas(1)
            .build();
    }
    
    @Bean
    public NewTopic accountEventsTopic() {
        return TopicBuilder.name("account-events")
            .partitions(3)
            .replicas(1)
            .build();
    }
    
    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name("payment-events")
            .partitions(3)
            .replicas(1)
            .build();
    }
}