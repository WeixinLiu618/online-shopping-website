package com.shop.orderservice.repository;

import com.shop.orderservice.entity.OrdersByUser;
import com.shop.orderservice.entity.OrdersByUser.Key;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface OrdersByUserRepository extends CassandraRepository<OrdersByUser, Key> { }