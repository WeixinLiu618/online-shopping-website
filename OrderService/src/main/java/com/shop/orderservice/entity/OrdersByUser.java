package com.shop.orderservice.entity;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.math.BigDecimal;
import java.util.UUID;

@Table("orders_by_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersByUser {

    @PrimaryKey
    private Key key;

    @Column("order_id")
    private UUID orderId;

    private String status;

    @Column("total_amount")
    private BigDecimal totalAmount;

    private String currency;

    @PrimaryKeyClass
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Key {
        @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        // timeuuid for descending time order
        @PrimaryKeyColumn(name = "created_at_ts", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private UUID createdAtTs;
    }
}
