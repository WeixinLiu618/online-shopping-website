package com.shop.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
//@EnableCassandraRepositories(basePackages = "com.shop.orderservice.repository")
@EnableFeignClients(basePackages = "com.shop.orderservice.client")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
