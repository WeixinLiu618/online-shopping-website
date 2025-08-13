package com.shop.orderservice.entity;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.Map;
import java.util.UUID;

@Table("order_status_history_by_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryByOrder {

    @PrimaryKey
    private Key key;

    private String status;
    private String reason;
    @Column("actor")
    private String actor;                 // system/user/admin
    @Column("metadata")
    private Map<String, String> metadata;

    @PrimaryKeyClass
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Key {
        @PrimaryKeyColumn(name = "order_id", type = PrimaryKeyType.PARTITIONED)
        private UUID orderId;

        // timeline per order
        @PrimaryKeyColumn(name = "event_ts", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
        private UUID eventTs; // timeuuid
    }
}
