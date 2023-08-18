package com.anryus.publish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.anryus.publish")
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan({"com.anryus.common","com.anryus.publish"})
public class PublishApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublishApplication.class);
    }
}
