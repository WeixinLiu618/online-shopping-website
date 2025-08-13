package com.shop.orderservice.payload;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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