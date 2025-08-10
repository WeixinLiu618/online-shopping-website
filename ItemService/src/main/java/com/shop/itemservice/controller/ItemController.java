package com.shop.itemservice.controller;

import com.shop.itemservice.payload.CreateItemRequest;
import com.shop.itemservice.payload.ItemDto;
import com.shop.itemservice.payload.UpdateInventoryRequest;
import com.shop.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.shop.itemservice.util.PaginationUtil.*;


@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 1. 获取分页列表
    @GetMapping
    public Page<ItemDto> getItems(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER + "") int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE + "") int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION) String sortDir
    ) {
        return itemService.getAllItems(pageNo, pageSize, sortBy, sortDir);
    }

    // 2. 获取单个商品
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable UUID itemId) {
        return itemService.getItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // 3. 创建商品
    @PostMapping
    public ItemDto createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    // 4. 查看库存
    @GetMapping("/{itemId}/inventory")
    public int getInventory(@PathVariable UUID itemId) {
        return itemService.getInventory(itemId);
    }

    // 5. 更新库存
    @PatchMapping("/{itemId}/inventory")
    public int updateInventory(
            @PathVariable UUID itemId,
            @RequestBody UpdateInventoryRequest request
    ) {
        return itemService.updateInventory(itemId, request);
    }
}
