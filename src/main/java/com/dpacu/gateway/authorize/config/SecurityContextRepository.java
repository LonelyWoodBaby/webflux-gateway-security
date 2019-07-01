package com.dpacu.gateway.authorize.config;

import com.dpacu.gateway.authorize.details.domain.PermissionDomain;
import com.dpacu.gateway.authorize.details.domain.status.RequestMethodType;
import com.dpacu.gateway.authorize.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository {
    @Value("${neptune.authorize.permission.enable:true}")
    private boolean enablePermission;
    @Value("${neptune.authorize.enable:true}")
    private boolean enableAuthorize;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Qualifier("permissionRedisTemplate")
    private RedisTemplate<String, PermissionDomain> permissionDomainRedisTemplate;
    @Autowired
    private JWTUtil jwtUtil;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return this.authenticationManager.authenticate(auth)
                    .filter(authentication -> (!enablePermission) || validatePermission(authToken,exchange.getRequest()))
                    .map(SecurityContextImpl::new);
        } else {
            return Mono.empty();
        }
    }
    private boolean validatePermission(String authToken,ServerHttpRequest request){
        //判断permission权限
        Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
        String username = jwtUtil.getUsernameFromToken(claims);
        String permissionKey = "perm_"+username + "_" + claims.get(JWTUtil.TOKEN_ID);
        List<PermissionDomain> permissionList = permissionDomainRedisTemplate.opsForList().range(permissionKey,0,-1);
        if(permissionList != null){
            String requestPath = (request.getPath().value().startsWith("/api"))?request.getPath().value().substring(4):request.getPath().value();
            Optional<PermissionDomain> hashPermission = permissionList.stream()
                    //TODO 是否允许PermissionUri以逗号隔离，暂时不做此处理
                    .filter(permission -> antPathMatcher.match(permission.getUriExpression(),requestPath))
                    .filter(permission -> ( permission.getRequestMethodType() == RequestMethodType.ALL
                            || request.getMethod().name().equalsIgnoreCase(permission.getRequestMethodType().name())))
                    .findAny();
            log.error("请求路径：" + request.getPath().value() +",请求方式：" + request.getMethod().name() + ",判断结果：" + hashPermission.isPresent());
            return hashPermission.isPresent();
        }
        return false;
    }
}
