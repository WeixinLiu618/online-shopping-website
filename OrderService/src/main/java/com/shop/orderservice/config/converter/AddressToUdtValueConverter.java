package com.shop.orderservice.config.converter;

import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.CqlSession;
import com.shop.orderservice.entity.udt.Address;
import org.springframework.core.convert.converter.Converter;

public class AddressToUdtValueConverter implements Converter<Address, UdtValue> {

    private final CqlSession session;

    public AddressToUdtValueConverter(CqlSession session) {
        this.session = session;
    }

    @Override
    public UdtValue convert(Address address) {
        UserDefinedType addressType = session.getMetadata()
                .getKeyspace(session.getKeyspace().get())
                .flatMap(ks -> ks.getUserDefinedType("address"))
                .orElseThrow(() -> new IllegalArgumentException("UDT 'address' not found"));

        return addressType.newValue()
                .setString("name", address.getName())
                .setString("phone", address.getPhone())
                .setString("line1", address.getLine1())
                .setString("line2", address.getLine2())
                .setString("city", address.getCity())
                .setString("state", address.getState())
                .setString("postal_code", address.getPostalCode())
                .setString("country", address.getCountry());
    }
}

