package com.enefit.storetask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity for storing item audit trail.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long itemId;
    private String itemName;
    private String action;
    private double price;
    private int quantity;
    private int stockLevel;
    private String description;
    private LocalDateTime timestamp;
} 