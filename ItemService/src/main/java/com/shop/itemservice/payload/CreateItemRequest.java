package com.shop.itemservice.payload;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequest {
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String upc;
    private List<String> imageUrls;
    private int availableQuantity;
    private String category;
    private List<String> tags;
}
