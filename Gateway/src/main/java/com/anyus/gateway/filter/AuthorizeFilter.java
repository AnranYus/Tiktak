package com.anyus.gateway.filter;

import com.anryus.common.entity.Rest;
import com.anryus.common.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
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
public class AuthorizeFilter extends AbstractGatewayFilterFactory<Object> implements Ordered {

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
            AtomicReference<String> token = new AtomicReference<>();

            token.set(request.getQueryParams().getFirst("token"));
            if (token.get() != null && !Objects.equals(token.get(), "")){
                Map<String, String> verify = jwtUtils.verify(token.get());
                String uid = verify.get("uid");
                request.mutate().header("user-id",uid).build();
                return chain.filter(exchange.mutate().request(request).build());
            }



            return doReject(exchange);
        };
    }

    private Mono<Void> doReject(ServerWebExchange exchange) {
        Rest<String> rest = Rest.fail( "拒绝访问");
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

    @Override
    public int getOrder() {
        return 1;
    }
}