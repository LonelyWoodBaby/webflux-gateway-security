package com.dpacu.gateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WebfluxRoutesConfig {
    private static final String CLIENT_TEST_PATH = "/api/client";


    /**
     * 用户登陆，此时请求授权系统申请令牌。
     * 添加拦截器进行区分，登陆操作时为其赋值clientId以及ClientSecret，并返回令牌信息
     * 登出操作时获取token令牌，并进行注销
     * @param builder
     * @return
     */
    @Bean(name = "neptune-client-route")
    public RouteLocator authorizationLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path(CLIENT_TEST_PATH + "/**")
                        .filters( f -> f.stripPrefix(2))
//                        .uri("lb://neptune-AUTHORIZATION/")
                        .uri("http://localhost:10001/")
                        .order(2)
                        .id("neptune-client-route-config")
                ).build();
    }
}
