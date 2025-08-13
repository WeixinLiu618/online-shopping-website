package com.shop.orderservice.config.converter;

import com.shop.orderservice.entity.udt.Address;
import org.springframework.core.convert.converter.Converter;
import com.datastax.oss.driver.api.core.data.UdtValue;

public class UdtValueToAddressConverter implements Converter<UdtValue, Address> {
    @Override
    public Address convert(UdtValue udtValue) {
        return Address.builder()
                .name(udtValue.getString("name"))
                .phone(udtValue.getString("phone"))
                .line1(udtValue.getString("line1"))
                .line2(udtValue.getString("line2"))
                .city(udtValue.getString("city"))
                .state(udtValue.getString("state"))
                .postalCode(udtValue.getString("postal_code"))
                .country(udtValue.getString("country"))
                .build();
    }
}

