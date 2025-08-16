package com.shop.orderservice.entity.udt;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType("item_line")
public class ItemLine {

    @Column("item_id")
    private UUID itemId;
    private String name;

    @Column("unit_price")
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
    private String upc;

    @Column("image_url")
    private String imageUrl;
}
