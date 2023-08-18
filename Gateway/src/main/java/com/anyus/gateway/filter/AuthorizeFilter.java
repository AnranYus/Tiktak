package com.anyus.gateway.filter;

import com.anryus.common.entity.Rest;
import com.anryus.common.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Component
public class AuthorizeFilter extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    JwtUtils jwtUtils;
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            //检查是否拥有token
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getQueryParams().getFirst("token");
            if (token != null && !Objects.equals(token, "")){
                Map<String, String> verify = jwtUtils.verify(token);
                String uid = verify.get("uid");
                String rule = verify.get("rule");

                if (uid != null && rule != null){
                    //TODO 暂时的不做角色鉴权,因为客户端没有提供管理员角色
                   return   chain.filter(exchange);
                }
            }

            return doReject(exchange);
        };
    }

    private Mono<Void> doReject(ServerWebExchange exchange) {
        Rest<String> rest = Rest.fail( "拒绝访问",null);
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        DataBuffer wrap = dataBufferFactory.wrap(rest.toString().getBytes());
        return response.writeWith(Mono.just(wrap));
    }

}