package com.enefit.storetask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Model representing store events for Kafka messaging.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvent {
    private String action;
    private Long itemId;
    private String itemName;
    private double price;
    private int quantity;
    private int stockLevel;
    private String description;
    private LocalDateTime timestamp;
} 