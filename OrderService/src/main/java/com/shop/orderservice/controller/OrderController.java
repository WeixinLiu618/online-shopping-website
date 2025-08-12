package com.shop.orderservice.controller;

import com.shop.orderservice.payload.*;
import com.shop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderRequest req,
                                           @RequestHeader(value = "X-Actor", required = false) String actor) {
        var dto = orderService.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{orderId}")
    public OrderDto get(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderDto updateStatus(@PathVariable UUID orderId,
                                 @RequestBody UpdateOrderStatusRequest req,
                                 @RequestHeader(value = "X-Actor", required = false) String actor) {
        return orderService.updateStatus(orderId, req, actor == null ? "system" : actor);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID orderId,
                                       @RequestParam(required = false) String reason,
                                       @RequestHeader(value = "X-Actor", required = false) String actor) {
        orderService.cancelOrder(orderId, reason, actor == null ? "system" : actor);
        return ResponseEntity.noContent().build();
    }
}
