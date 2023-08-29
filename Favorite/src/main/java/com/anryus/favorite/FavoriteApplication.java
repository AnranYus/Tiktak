package com.anryus.favorite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.anryus.favorite.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class FavoriteApplication {
    public static void main(String[] args) {
        SpringApplication.run(FavoriteApplication.class);
    }
}
