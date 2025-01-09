package com.enefit.storetask.dto;

import lombok.Data;

@Data
public class ItemResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private Integer soldQuantity;
}