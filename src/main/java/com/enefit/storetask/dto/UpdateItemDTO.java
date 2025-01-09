package com.enefit.storetask.dto;

import lombok.Data;

@Data
public class UpdateItemDTO {
    private String name;

    private double price;

    private int quantity;
}