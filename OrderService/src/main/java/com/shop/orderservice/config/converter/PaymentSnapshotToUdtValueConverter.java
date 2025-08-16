package com.shop.orderservice.config.converter;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.shop.orderservice.entity.udt.PaymentSnapshot;
import org.springframework.core.convert.converter.Converter;

public class PaymentSnapshotToUdtValueConverter implements Converter<PaymentSnapshot, UdtValue> {
    private final CqlSession session;

    public PaymentSnapshotToUdtValueConverter(CqlSession session) {
        this.session = session;
    }

    @Override
    public UdtValue convert(PaymentSnapshot payment) {
        UserDefinedType udt = session.getMetadata()
                .getKeyspace(session.getKeyspace().get())
                .flatMap(ks -> ks.getUserDefinedType("payment_snapshot"))
                .orElseThrow(() -> new IllegalArgumentException("UDT 'payment_snapshot' not found"));

        return udt.newValue()
                .setString("method", payment.getMethod())
                .setBigDecimal("amount", payment.getAmount())
                .setString("currency", payment.getCurrency())
                .setString("txn_id", payment.getTxnId())
                .setString("status", payment.getStatus())
                .setString("provider", payment.getProvider());
    }
}

