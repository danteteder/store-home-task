package com.enefit.storetask.service;

import com.enefit.storetask.model.ItemAudit;
import com.enefit.storetask.model.StoreEvent;
import com.enefit.storetask.repository.ItemAuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for consuming and logging store events from Kafka
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreEventConsumer {

    private final ItemAuditRepository auditRepository;

    @KafkaListener(topics = "store-events", groupId = "store-group")
    public void handleEvent(StoreEvent event) {
        log.info("Received event: {}", event);
        
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
            
        auditRepository.save(audit);
        
        log.info("Stored audit: {}", audit);
    }
} 