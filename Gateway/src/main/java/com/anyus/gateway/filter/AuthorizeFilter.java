package com.anyus.gateway.filter;

import com.anryus.common.entity.Rest;
import com.anryus.common.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AuthorizeFilter extends AbstractGatewayFilterFactory<Object> {

    final
    JwtUtils jwtUtils;

    public AuthorizeFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            //检查是否拥有token
            ServerHttpRequest request = exchange.getRequest();
            HttpMethod method = request.getMethod();
            AtomicReference<String> token = new AtomicReference<>();

            if (method == HttpMethod.GET){
                token.set(request.getQueryParams().getFirst("token"));

                if (token.get() != null && !Objects.equals(token.get(), "")){
                    Map<String, String> verify = jwtUtils.verify(token.get());
                    String uid = verify.get("uid");
                    String rule = verify.get("rule");

                    if (uid != null && rule != null){
                        //TODO 暂时的不做角色鉴权,因为客户端没有提供管理员角色
                        return   chain.filter(exchange);
                    }
                }
            }

            if (method == HttpMethod.POST){
                //TODO 暂时无法从Body中获取token，需要新的解决方案
                return   chain.filter(exchange);
            }



            return doReject(exchange);
        };
    }

    private Mono<Void> doReject(ServerWebExchange exchange) {
        Rest<String> rest = Rest.fail( "拒绝访问",null);
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        byte[] data = new byte[0];
        try {
            data = mapper.writeValueAsBytes(rest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Flux<DataBuffer> body = Flux.just(dataBufferFactory.wrap(data));
        return response.writeWith(Mono.just(body).block());
    }

}