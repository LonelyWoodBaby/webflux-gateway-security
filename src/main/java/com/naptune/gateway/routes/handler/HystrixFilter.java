package com.naptune.gateway.routes.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.HystrixGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class HystrixFilter implements GlobalFilter, Ordered {

    @Autowired
    private HystrixGatewayFilterFactory hystrixGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String[] gatewayPaths = exchange.getRequest().getPath().value().split("/");
        if(gatewayPaths.length > 0 ){
            HystrixGatewayFilterFactory.Config hystrixConfig = new HystrixGatewayFilterFactory.Config();
            hystrixConfig.setFallbackUri("forward:/fallback/timeOut/" + gatewayPaths[1]);
            hystrixConfig.setName("hystrixFilter");
            return hystrixGatewayFilterFactory.apply(hystrixConfig).filter(exchange,chain);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE -1;
    }
}
