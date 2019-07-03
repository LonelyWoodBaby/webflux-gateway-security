package com.naptune.gateway.authorize.config;

import com.naptune.gateway.authorize.details.domain.AdminDomain;
import com.naptune.gateway.authorize.details.domain.PermissionDomain;
import com.naptune.gateway.authorize.details.domain.status.RequestMethodType;
import com.naptune.gateway.authorize.utils.JWTUtil;
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
import java.util.stream.Collectors;


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

    @Autowired
    private List<String> defaultManagerList;
    @Autowired
    private AdminDomain defaultManagerDomain;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
    五矿信托特殊要求：
    1. 给小微提供万能Token，以便小微无需登陆即可请求。此token不不过期，不考虑安全问题。
    2. 小微可访问Uri有限制，可理解为一个小微系统的特殊用户


    设计流程如下
    1.提供默认管理员配置项（DefaultManagerConfig）
    2.每次系统启动时，使用默认管理员用户名和密码进行登录，并获取jwt信息，保存下来。jwt与Redis权限过期时间为-1
    3.系统每次启用时，自动对所有默认管理员执行一次登陆操作，
     */

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
        List<PermissionDomain> permissionList = null;
        //如果username属于默认default用户，则走自己的权限校验，否则从redis中获取
        if(defaultManagerList!=null && defaultManagerList.contains(username)){
            permissionList = defaultManagerDomain.getRoleList().stream().flatMap(role -> role.getPermList().stream()).collect(Collectors.toList());
        }else {
            String permissionKey = "perm_"+username + "_" + claims.get(JWTUtil.TOKEN_ID);
            permissionList = permissionDomainRedisTemplate.opsForList().range(permissionKey,0,-1);
        }
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
