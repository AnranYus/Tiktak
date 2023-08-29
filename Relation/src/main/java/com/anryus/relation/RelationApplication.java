package com.anryus.relation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.anryus.relation.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan({"com.anryus.common","com.anryus.relation"})
@SpringBootApplication
public class RelationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RelationApplication.class);
    }
}
