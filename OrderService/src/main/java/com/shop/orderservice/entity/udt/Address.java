package com.shop.orderservice.entity.udt;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String name;
    private String phone;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
