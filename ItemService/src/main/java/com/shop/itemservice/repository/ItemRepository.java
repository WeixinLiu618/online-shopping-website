package com.shop.itemservice.repository;


import com.shop.itemservice.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends MongoRepository<Item, String> {

    Optional<Item> findByItemId(UUID itemId);

    Optional<Item> findByUpc(String upc);

    boolean existsByUpc(String upc);
}
