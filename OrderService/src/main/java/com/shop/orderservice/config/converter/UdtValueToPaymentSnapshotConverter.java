package com.shop.orderservice.config.converter;

import com.datastax.oss.driver.api.core.data.UdtValue;
import com.shop.orderservice.entity.udt.PaymentSnapshot;
import org.springframework.core.convert.converter.Converter;

public class UdtValueToPaymentSnapshotConverter implements Converter<UdtValue, PaymentSnapshot> {
    @Override
    public PaymentSnapshot convert(UdtValue udt) {
        return PaymentSnapshot.builder()
                .method(udt.getString("method"))
                .amount(udt.getBigDecimal("amount"))
                .currency(udt.getString("currency"))
                .txnId(udt.getString("txn_id"))
                .status(udt.getString("status"))
                .provider(udt.getString("provider"))
                .build();
    }
}

