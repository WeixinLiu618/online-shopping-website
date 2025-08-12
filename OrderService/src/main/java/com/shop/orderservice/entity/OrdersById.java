package com.shop.orderservice.entity;


import com.shop.orderservice.entity.udt.Address;
import com.shop.orderservice.entity.udt.ItemLine;
import com.shop.orderservice.entity.udt.PaymentSnapshot;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Table("orders_by_id")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrdersById {

    @PrimaryKey
    private UUID orderId;

    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private String status;                // enum name
    private BigDecimal totalAmount;
    private String currency;

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private Address shippingAddr;

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private Address billingAddr;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "item_line")
    private List<ItemLine> items;

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "payment_snapshot")
    private PaymentSnapshot payment;

    private String notes;
}
