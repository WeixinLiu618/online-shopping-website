package com.shop.orderservice.controller;

import com.shop.orderservice.payload.*;
import com.shop.orderservice.service.OrderService;
import com.shop.orderservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    /**
     * 创建订单：用户只能给自己下单；管理员可代客下单
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderRequest req, HttpServletRequest http) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        // 从 JWT 取 userId
        String h = http.getHeader("Authorization");
        UUID tokenUserId = (h != null && h.startsWith("Bearer ")) ? jwtUtil.userId(h.substring(7)) : null;

        if (!isAdmin) {
            if (tokenUserId == null || !tokenUserId.equals(req.getUserId())) {
                throw new AccessDeniedException("Cannot create order for other users");
            }
        }
        OrderDto dto = orderService.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * 查询订单：本人或管理员
     */
    @PreAuthorize("@orderAuthorization.canRead(#orderId)")
    @GetMapping("/{orderId}")
    public OrderDto get(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    /**
     * 更新状态：仅管理员
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{orderId}/status")
    public OrderDto updateStatus(@PathVariable UUID orderId, @RequestBody UpdateOrderStatusRequest req) {
        String actor = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.updateStatus(orderId, req, actor);
    }

    /**
     * 取消：本人或管理员
     */
    @PreAuthorize("@orderAuthorization.canCancel(#orderId)")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable UUID orderId,
                                       @RequestParam(required = false) String reason) {
        String actor = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.cancelOrder(orderId, reason, actor);
        return ResponseEntity.ok("Order cancel successfully!");
    }
}
