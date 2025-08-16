package com.shop.orderservice.entity.udt;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType(value = "address")
public class Address {
    private String name;
    private String phone;
    private String line1;
    private String line2;
    private String city;
    private String state;

    @Column("postal_code")
    private String postalCode;
    private String country;
}
