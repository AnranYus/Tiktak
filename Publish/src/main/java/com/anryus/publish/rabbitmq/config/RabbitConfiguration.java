package com.anryus.publish.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean("directExchange")  //定义交换机Bean，可以很多个
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("amq.direct").build();
    }

    @Bean("publishRollback")     //定义消息队列
    public Queue queue(){
        return QueueBuilder
                .durable("publishRollback")   //非持久化类型
                .build();
    }

    @Bean("binding")
    public Binding binding(@Qualifier("directExchange") Exchange exchange,
                           @Qualifier("publishRollback") Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("rollback")
                .noargs();
    }
}
