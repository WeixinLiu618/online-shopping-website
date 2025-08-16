package com.shop.orderservice.config;

import com.shop.orderservice.config.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.core.convert.converter.Converter;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackages = "com.shop.orderservice.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.shop.orderservice.entity", "com.shop.orderservice.entity.udt"};
    }

    @Override
    protected String getKeyspaceName() {
        return "shop";
    }

    @Bean
    public CassandraCustomConversions customConversions(CqlSession session) {
        return new CassandraCustomConversions(List.of(
                new AddressToUdtValueConverter(session),
                new UdtValueToAddressConverter(),
                new ItemLineToUdtValueConverter(session),
                new UdtValueToItemLineConverter(),
                new PaymentSnapshotToUdtValueConverter(session),
                new UdtValueToPaymentSnapshotConverter()
        ));
    }


}
