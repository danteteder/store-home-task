package com.enefit.storetask.service;

import com.enefit.storetask.model.ItemAudit;
import com.enefit.storetask.model.StoreEvent;
import com.enefit.storetask.repository.ItemAuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for consuming and logging store events from Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreEventConsumer {

    private final ItemAuditRepository itemAuditRepository;

    /**
     * Handles incoming store events from Kafka.
     *
     * @param event the store event
     */
    @KafkaListener(topics = "store-events", groupId = "store-group")
    public void handleEvent(StoreEvent event) {
        log.info("Received event: {}", event);
        
        try {
            ItemAudit audit = ItemAudit.builder()
                .itemId(event.getItemId())
                .itemName(event.getItemName())
                .action(event.getAction())
                .price(event.getPrice())
                .quantity(event.getQuantity())
                .stockLevel(event.getStockLevel())
                .description(event.getDescription())
                .timestamp(event.getTimestamp())
                .build();
                
            itemAuditRepository.save(audit);
            
            log.info("Stored audit: {}", audit);
        } catch (Exception e) {
            log.error("Failed to process event: {}", event, e);
            // Consider adding to a dead-letter queue or retry mechanism
        }
    }
} 