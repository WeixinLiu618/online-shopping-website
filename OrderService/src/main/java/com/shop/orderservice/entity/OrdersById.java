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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersById {

    @PrimaryKey("order_id")
    private UUID orderId;

    @Column("user_id")
    private UUID userId;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
    private String status;

    @Column("total_amount")
    private BigDecimal totalAmount;
    private String currency;

    @Column("shipping_addr")
    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private Address shippingAddr;

    @Column("billing_addr")
    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private Address billingAddr;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "item_line")
    private List<ItemLine> items;

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "payment_snapshot")
    private PaymentSnapshot payment;

    private String notes;
}
