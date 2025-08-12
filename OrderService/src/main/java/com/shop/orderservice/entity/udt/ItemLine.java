package com.shop.orderservice.entity.udt;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import java.math.BigDecimal;
import java.util.UUID;

@UserDefinedType("item_line")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ItemLine {
    private UUID itemId;
    private String name;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
    private String upc;
    private String imageUrl;
}
