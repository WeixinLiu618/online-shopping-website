package com.shop.orderservice.config.converter;

import com.datastax.oss.driver.api.core.data.UdtValue;
import com.shop.orderservice.entity.udt.ItemLine;
import org.springframework.core.convert.converter.Converter;

public class UdtValueToItemLineConverter implements Converter<UdtValue, ItemLine> {
    @Override
    public ItemLine convert(UdtValue udt) {
        return ItemLine.builder()
                .itemId(udt.getUuid("item_id"))
                .name(udt.getString("name"))
                .unitPrice(udt.getBigDecimal("unit_price"))
                .quantity(udt.getInt("quantity"))
                .subtotal(udt.getBigDecimal("subtotal"))
                .upc(udt.getString("upc"))
                .imageUrl(udt.getString("image_url"))
                .build();
    }
}

