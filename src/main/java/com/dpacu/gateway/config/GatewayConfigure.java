package com.dpacu.gateway.config;

import com.dpacu.gateway.authorize.details.domain.PermissionDomain;
import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class GatewayConfigure {
    /**
     * 启动负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate rest(){
        return new RestTemplate();
    }

    /**
     * 自定义配置ribbon负载均衡算法
     */
    @Bean
    @Scope("prototype")
    public IRule myRule(){
        log.info("启动BestAvailableRule均衡算法");
        //先过滤掉由于多次访问故障而处于跳闸状态的服务，选择一个并发量最小的服务
        return new BestAvailableRule();
    }

    /**
     * 开启feign日志等级配置
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean("permissionRedisTemplate")
    public RedisTemplate<String, PermissionDomain> permissionRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, PermissionDomain> permissionRedisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<PermissionDomain> serializer = new Jackson2JsonRedisSerializer<PermissionDomain>(PermissionDomain.class);
        permissionRedisTemplate.setValueSerializer(serializer);
        permissionRedisTemplate.setHashKeySerializer(serializer);
        permissionRedisTemplate.setKeySerializer(new StringRedisSerializer());
        permissionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());

        permissionRedisTemplate.setConnectionFactory(connectionFactory);
        return permissionRedisTemplate;
    }
}
