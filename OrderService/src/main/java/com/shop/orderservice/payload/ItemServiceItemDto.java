package com.shop.orderservice.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ItemServiceItemDto {
    private UUID itemId;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String upc;
    private List<String> imageUrls;
    private int availableQuantity;
    private String category;
    private List<String> tags;
}