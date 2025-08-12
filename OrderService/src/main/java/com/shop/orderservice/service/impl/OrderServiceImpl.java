package com.shop.orderservice.service.impl;


import com.datastax.oss.driver.api.core.uuid.Uuids;

import com.shop.orderservice.entity.*;
import com.shop.orderservice.entity.udt.*;
import com.shop.orderservice.payload.*;
import com.shop.orderservice.repository.*;
import com.shop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersByIdRepository orderByIdRepo;
    private final OrdersByUserRepository orderByUserRepo;
    private final OrderStatusHistoryRepository orderStatusHistoryRepo;
    private final ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest req) {
        var now = Instant.now();
        var orderId = UUID.randomUUID();
        var createdTs = Uuids.timeBased();

        // map items & compute total
        List<ItemLine> lines = req.getItems().stream()
                .map(i -> ItemLine.builder()
                        .itemId(i.getItemId())
                        .name(i.getName())
                        .unitPrice(i.getUnitPrice())
                        .quantity(i.getQuantity())
                        .subtotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .upc(i.getUpc())
                        .imageUrl(i.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = lines.stream()
                .map(ItemLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var orderById = OrdersById.builder()
                .orderId(orderId)
                .userId(req.getUserId())
                .createdAt(now)
                .updatedAt(now)
                .status(OrderStatus.CREATED.name())
                .totalAmount(total)
                .currency(req.getCurrency())
                .shippingAddr(mapper.map(req.getShippingAddr(), Address.class))
                .billingAddr(mapper.map(req.getBillingAddr(), Address.class))
                .items(lines)
                .notes(req.getNotes())
                .build();
        orderByIdRepo.save(orderById);

        var key = OrdersByUser.Key.builder()
                .userId(req.getUserId())
                .createdAtTs(createdTs)
                .build();
        var ordersByUser = OrdersByUser.builder()
                .key(key)
                .orderId(orderId)
                .status(OrderStatus.CREATED.name())
                .totalAmount(total)
                .currency(req.getCurrency())
                .build();
        orderByUserRepo.save(ordersByUser);

        var historyKey = OrderStatusHistoryByOrder.Key.builder().orderId(orderId).eventTs(createdTs).build();
        var history = OrderStatusHistoryByOrder.builder()
                .key(historyKey)
                .status(OrderStatus.CREATED.name())
                .actor("system")
                .build();
        orderStatusHistoryRepo.save(history);

        return mapper.map(orderById, OrderDto.class);
    }

    @Override
    public OrderDto getOrder(UUID orderId) {
        var e = orderByIdRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.map(e, OrderDto.class);
    }

    @Override
    public List<OrderDto> listOrdersByUser(UUID userId, int pageSize, String pagingState) {
        // TODO: implement driver paging. For now, leave unimplemented or return empty list.
        throw new UnsupportedOperationException("Implement Cassandra paging with PagingState");
    }

    @Override
    public OrderDto updateStatus(UUID orderId, UpdateOrderStatusRequest req, String actor) {
        var orderById = orderByIdRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderById.setStatus(req.getStatus());
        orderById.setUpdatedAt(Instant.now());
        orderByIdRepo.save(orderById);

        var hk = OrderStatusHistoryByOrder.Key.builder().orderId(orderId).eventTs(Uuids.timeBased()).build();
        var hist = OrderStatusHistoryByOrder.builder()
                .key(hk)
                .status(req.getStatus())
                .reason(req.getReason())
                .actor(actor)
                .build();
        orderStatusHistoryRepo.save(hist);

        return mapper.map(orderById, OrderDto.class);
    }

    @Override
    public void cancelOrder(UUID orderId, String reason, String actor) {
        updateStatus(orderId, UpdateOrderStatusRequest.builder()
                .status(OrderStatus.CANCELLED.name())
                .reason(reason)
                .build(), actor);
    }
}