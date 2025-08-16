package com.shop.orderservice.client;

import com.shop.orderservice.payload.ItemServiceItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "item-service",
        configuration = ItemFeignConfig.class // JWT 转发 & Feign 日志
)
public interface ItemClient {

    @GetMapping("/api/items/{itemId}")
    ItemServiceItemDto getItem(@PathVariable("itemId") UUID itemId);

    @GetMapping("/api/items/{itemId}/inventory")
    Integer getInventory(@PathVariable("itemId") UUID itemId);
}
