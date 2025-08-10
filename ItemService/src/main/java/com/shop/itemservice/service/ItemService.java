package com.shop.itemservice.service;

import com.shop.itemservice.payload.CreateItemRequest;
import com.shop.itemservice.payload.ItemDto;
import com.shop.itemservice.payload.UpdateInventoryRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {

    ItemDto createItem(CreateItemRequest request);

    Optional<ItemDto> getItemById(UUID itemId);

    Page<ItemDto> getAllItems(int pageNo, int pageSize, String sortBy, String sortDir);


    int getInventory(UUID itemId);

    int updateInventory(UUID itemId, UpdateInventoryRequest request);
}
