package com.shop.itemservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Document(collection = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    private String id; // MongoDB 内部 ID (ObjectId 或 UUID 字符串)

    private UUID itemId;           // primary key，UUID 格式
    private String name;           // 商品名称
    private String description;    // 商品描述
    private BigDecimal unitPrice; // 单价
    private String upc;            // 通用产品编码

    private List<String> imageUrls; // 商品图片链接

    private int availableQuantity;  // 库存
    private int orderCount;      //销量

    private String category;       // 分类（optional）
    private List<String> tags;     // 标签（可选）
}

