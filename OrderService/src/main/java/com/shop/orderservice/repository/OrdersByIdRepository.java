package com.shop.orderservice.repository;

import com.shop.orderservice.entity.OrdersById;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface OrdersByIdRepository extends CassandraRepository<OrdersById, UUID> {
}

