package com.shop.orderservice.service.impl;


import com.datastax.oss.driver.api.core.uuid.Uuids;

import com.shop.orderservice.client.ItemClient;
import com.shop.orderservice.entity.*;
import com.shop.orderservice.entity.udt.*;
import com.shop.orderservice.event.KafkaTopics;
import com.shop.orderservice.event.OrderEvent;
import com.shop.orderservice.exception.InsufficientInventoryException;
import com.shop.orderservice.kafka.OrderEventProducer;
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

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersByIdRepository orderByIdRepo;
    private final OrdersByUserRepository orderByUserRepo;
    private final OrderStatusHistoryRepository orderStatusHistoryRepo;
    private final ModelMapper mapper;
    private final ItemClient itemClient;
    private final OrderEventProducer eventProducer;

    @Override
    public OrderDto createOrder(CreateOrderRequest req) {
        var now = Instant.now();
        var orderId = UUID.randomUUID();
        var createdTs = Uuids.timeBased(); // timeuuid，用于用户订单列表和历史时间线

        // 1) 行项目校验：存在性 + 库存；价格以 Item Service 为准（忽略客户端传入）
        List<ItemLine> lines = req.getItems().stream()
                .map(reqLine -> {
                    ItemServiceItemDto item = itemClient.getItem(reqLine.getItemId()); // 不存在将抛 FeignException.NotFound
                    Integer available = itemClient.getInventory(reqLine.getItemId());
                    if (available == null) available = 0;
                    if (available < reqLine.getQuantity()) {
                        throw new InsufficientInventoryException(
                                "Insufficient inventory for %s: need %d, have %d"
                                        .formatted(item.getName(), reqLine.getQuantity(), available));
                    }
                    BigDecimal unitPrice = item.getUnitPrice(); // 以 Item Service 价格为准
                    return ItemLine.builder()
                            .itemId(item.getItemId())
                            .name(item.getName())
                            .unitPrice(unitPrice)
                            .quantity(reqLine.getQuantity())
                            .subtotal(unitPrice.multiply(BigDecimal.valueOf(reqLine.getQuantity())))
                            .upc(item.getUpc())
                            .imageUrl(firstOrNull(item.getImageUrls()))
                            .build();
                })
                .toList();


        // 2) 计算总价
        BigDecimal total = lines.stream()
                .map(ItemLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3) 写 orders_by_id（详情快照）
        OrdersById obid = OrdersById.builder()
                .orderId(orderId)
                .userId(req.getUserId())
                .createdAt(now)
                .updatedAt(now)
                .status(OrderStatus.CREATED.name())
                .totalAmount(total)
                .currency(req.getCurrency())
                .shippingAddr(safeMapAddress(req.getShippingAddr()))
                .billingAddr(safeMapAddress(req.getBillingAddr()))
                .items(lines)
                .notes(req.getNotes())
                .build();
        orderByIdRepo.save(obid);

        // 4) 写 orders_by_user（列表视图）
        OrdersByUser.Key key = OrdersByUser.Key.builder()
                .userId(req.getUserId())
                .createdAtTs(createdTs)
                .build();
        OrdersByUser obu = OrdersByUser.builder()
                .key(key)
                .orderId(orderId)
                .status(OrderStatus.CREATED.name())
                .totalAmount(total)
                .currency(req.getCurrency())
                .build();
        orderByUserRepo.save(obu);

        // 5) 写 order_status_history_by_order（状态时间线）
        OrderStatusHistoryByOrder.Key hk = OrderStatusHistoryByOrder.Key.builder()
                .orderId(orderId)
                .eventTs(createdTs)
                .build();
        OrderStatusHistoryByOrder hist = OrderStatusHistoryByOrder.builder()
                .key(hk)
                .status(OrderStatus.CREATED.name())
                .actor("system")
                .build();
        orderStatusHistoryRepo.save(hist);

        // send kafka msg
        eventProducer.sendEvent(KafkaTopics.ORDER_CREATED, OrderEvent.builder()
                .orderId(orderId)
                .userId(req.getUserId())
                .status(OrderStatus.CREATED.name())
                .amount(total)
                .currency(req.getCurrency())
                .createdAt(now)
                .build());


        // 6) 返回 DTO
        return mapper.map(obid, OrderDto.class);
    }

    @Override
    public OrderDto getOrder(UUID orderId) {
        OrdersById e = orderByIdRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.map(e, OrderDto.class);
    }

    @Override
    public List<OrderDto> listOrdersByUser(UUID userId, int pageSize, String pagingState) {
        // TODO: implement driver paging. For now, leave unimplemented or return empty list.
        // 提示：这里推荐用 Spring Data Cassandra + PagingState 实现真正分页。
        // 先留空实现，避免引入额外依赖；需要时我可以给你完整分页代码。
        throw new UnsupportedOperationException("Implement Cassandra paging with PagingState");
    }

    @Override
    public OrderDto updateStatus(UUID orderId, UpdateOrderStatusRequest req, String actor) {
        OrdersById orderById = orderByIdRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderById.setStatus(req.getStatus());
        orderById.setUpdatedAt(Instant.now());
        orderByIdRepo.save(orderById);

        OrderStatusHistoryByOrder.Key hk = OrderStatusHistoryByOrder.Key.builder().orderId(orderId).eventTs(Uuids.timeBased()).build();
        OrderStatusHistoryByOrder hist = OrderStatusHistoryByOrder.builder()
                .key(hk)
                .status(req.getStatus())
                .reason(req.getReason())
                .actor(actor)
                .build();
        orderStatusHistoryRepo.save(hist);

        // === Kafka event emit ===
        if (OrderStatus.PAID.name().equals(req.getStatus())) {
            eventProducer.sendEvent(KafkaTopics.ORDER_PAID, OrderEvent.builder()
                    .orderId(orderId)
                    .userId(orderById.getUserId())
                    .status(OrderStatus.PAID.name())
                    .amount(orderById.getTotalAmount())
                    .currency(orderById.getCurrency())
                    .createdAt(orderById.getCreatedAt())
                    .build());
        }

        if (OrderStatus.CANCELLED.name().equals(req.getStatus())) {
            eventProducer.sendEvent(KafkaTopics.ORDER_CANCELLED, OrderEvent.builder()
                    .orderId(orderId)
                    .userId(orderById.getUserId())
                    .status(OrderStatus.CANCELLED.name())
                    .amount(orderById.getTotalAmount())
                    .currency(orderById.getCurrency())
                    .createdAt(orderById.getCreatedAt())
                    .build());
        }


        return mapper.map(orderById, OrderDto.class);
    }

    @Override
    public void cancelOrder(UUID orderId, String reason, String actor) {
        updateStatus(orderId, UpdateOrderStatusRequest.builder()
                .status(OrderStatus.CANCELLED.name())
                .reason(reason)
                .build(), actor);
    }

    // =============== helpers ===============

    private Address safeMapAddress(Object dto) {
        if (dto == null) return null;
        return mapper.map(dto, Address.class);
    }

    private String firstOrNull(List<String> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }
}