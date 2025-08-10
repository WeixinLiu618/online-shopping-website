package com.shop.itemservice.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryRequest {
    private int change; // 正数增加库存，负数减少库存
}
