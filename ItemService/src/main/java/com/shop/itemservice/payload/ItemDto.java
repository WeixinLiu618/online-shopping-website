package com.shop.itemservice.payload;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private UUID itemId;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String upc;
    private List<String> imageUrls;
    private int availableQuantity;
    private int orderCount;
    private String category;
    private List<String> tags;

}
