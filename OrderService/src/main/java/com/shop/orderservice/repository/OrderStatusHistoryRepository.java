package com.shop.orderservice.repository;


import com.shop.orderservice.entity.OrderStatusHistoryByOrder;
import com.shop.orderservice.entity.OrderStatusHistoryByOrder.Key;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface OrderStatusHistoryRepository extends CassandraRepository<OrderStatusHistoryByOrder, Key> { }
