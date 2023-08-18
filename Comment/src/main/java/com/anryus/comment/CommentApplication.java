package com.anryus.comment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.anryus.comment.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan({"com.anryus.common","com.anryus.comment"})
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class);
    }
}
