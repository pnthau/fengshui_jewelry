package com.fengshui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private String material;
    private String imageURL;
    private String youtubeURL;
    private String status;
    private String description;
    private Set<String> elements;
}
