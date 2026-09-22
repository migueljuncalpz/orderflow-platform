package com.migueljuncalp.inventoryservice.config;

import com.migueljuncalp.inventoryservice.domain.InsufficientStockException;
import com.migueljuncalp.inventoryservice.service.ProcessingInterruptedException;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class KafkaConfig {

    public static final String STOCK_MOVEMENTS_TOPIC = "stock-movements";

    @Bean
    NewTopic stockMovementsTopic() {
        return TopicBuilder.name(STOCK_MOVEMENTS_TOPIC).partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic stockMovementsDltTopic() {
        return TopicBuilder.name(STOCK_MOVEMENTS_TOPIC + "-dlt").partitions(3).replicas(1).build();
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        var backOff = new ExponentialBackOffWithMaxRetries(2);
        backOff.setInitialInterval(500L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(2_000L);

        var handler = new DefaultErrorHandler(recoverer, backOff);
        handler.addNotRetryableExceptions(InsufficientStockException.class, IllegalArgumentException.class,
                ProcessingInterruptedException.class);
        return handler;
    }
}
