package com.shop.orderservice.service;

import com.shop.orderservice.payload.CreateOrderRequest;
import com.shop.orderservice.payload.OrderDto;
import com.shop.orderservice.payload.UpdateOrderStatusRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequest req);   // calc totals, status=CREATED

    OrderDto getOrder(UUID orderId);

    List<OrderDto> listOrdersByUser(UUID userId, int pageSize, String pagingState); // simple paging

    OrderDto updateStatus(UUID orderId, UpdateOrderStatusRequest req, String actor); // append history

    void cancelOrder(UUID orderId, String reason, String actor);
}