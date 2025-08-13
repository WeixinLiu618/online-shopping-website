package com.shop.orderservice.config.converter;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.shop.orderservice.entity.udt.ItemLine;
import org.springframework.core.convert.converter.Converter;

public class ItemLineToUdtValueConverter implements Converter<ItemLine, UdtValue> {
    private final CqlSession session;

    public ItemLineToUdtValueConverter(CqlSession session) {
        this.session = session;
    }

    @Override
    public UdtValue convert(ItemLine item) {
        UserDefinedType udt = session.getMetadata()
                .getKeyspace(session.getKeyspace().get())
                .flatMap(ks -> ks.getUserDefinedType("item_line"))
                .orElseThrow(() -> new IllegalArgumentException("UDT 'item_line' not found"));

        return udt.newValue()
                .setUuid("item_id", item.getItemId())
                .setString("name", item.getName())
                .setBigDecimal("unit_price", item.getUnitPrice())
                .setInt("quantity", item.getQuantity())
                .setBigDecimal("subtotal", item.getSubtotal())
                .setString("upc", item.getUpc())
                .setString("image_url", item.getImageUrl());
    }
}

