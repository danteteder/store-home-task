package com.enefit.storetask.service;

import com.enefit.storetask.model.StoreEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for producing store events to Kafka
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreEventProducer {
    
    private final KafkaTemplate<String, StoreEvent> kafkaTemplate;
    private static final String TOPIC = "store-events";

    public void sendEvent(String action, Long itemId, String itemName, double price, 
                         int quantity, int stockLevel, String description) {
        StoreEvent event = StoreEvent.builder()
            .action(action)
            .itemId(itemId)
            .itemName(itemName)
            .price(price)
            .quantity(quantity)
            .stockLevel(stockLevel)
            .description(description)
            .timestamp(LocalDateTime.now())
            .build();

        log.info("Producing event: {}", event);
        
        kafkaTemplate.send(TOPIC, event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Event sent successfully: {}", event);
                } else {
                    log.error("Failed to send event: {}", ex.getMessage());
                }
            });
    }
} 