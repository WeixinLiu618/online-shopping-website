package com.shop.itemservice.service.impl;

import com.shop.itemservice.entity.Item;
import com.shop.itemservice.payload.CreateItemRequest;
import com.shop.itemservice.payload.ItemDto;
import com.shop.itemservice.payload.UpdateInventoryRequest;
import com.shop.itemservice.repository.ItemRepository;
import com.shop.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public ItemDto createItem(CreateItemRequest request) {
        Item item = Item.builder()
                .itemId(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .upc(request.getUpc())
                .imageUrls(request.getImageUrls())
                .availableQuantity(request.getAvailableQuantity())
                .category(request.getCategory())
                .tags(request.getTags())
                .build();

        Item saved = itemRepository.save(item);
        return modelMapper.map(saved, ItemDto.class);
    }

    @Override
    public Optional<ItemDto> getItemById(UUID itemId) {
        return itemRepository.findByItemId(itemId)
                .map(item -> modelMapper.map(item, ItemDto.class));
    }


    // sort by name, unitPrice, orderCount
    @Override
    public Page<ItemDto> getAllItems(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return itemRepository.findAll(pageable)
                .map(item -> modelMapper.map(item, ItemDto.class));
    }

    @Override
    public int getInventory(UUID itemId) {
        return itemRepository.findByItemId(itemId)
                .map(Item::getAvailableQuantity)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public int updateInventory(UUID itemId, UpdateInventoryRequest request) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        int newQty = item.getAvailableQuantity() + request.getChange();
        if (newQty < 0) {
            throw new IllegalArgumentException("Inventory cannot go below zero");
        }

        item.setAvailableQuantity(newQty);
        itemRepository.save(item);
        return newQty;
    }
}
