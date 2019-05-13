package com.dpacu.gateway.authorize.controller;

import com.dpacu.gateway.authorize.controller.model.AuthRequest;
import com.dpacu.gateway.authorize.controller.model.AuthResponse;
import com.dpacu.gateway.authorize.details.AdminDetails;
import com.dpacu.gateway.authorize.details.domain.PermissionDomain;
import com.dpacu.gateway.authorize.details.service.ReactAdminDetailsService;
import com.dpacu.gateway.authorize.utils.JWTUtil;
import com.dpacu.gateway.authorize.utils.TokenType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AuthenticationREST {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReactAdminDetailsService userRepository;
    @Autowired
    @Qualifier("permissionRedisTemplate")
    private RedisTemplate<String, PermissionDomain> permissionDomainRedisTemplate;
    @Value("${dpauc.oauth.jjwt.expiration.refresh}")
    private Long expirationRefreshTime;

    @RequestMapping(value = "/authorize/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return userRepository.findByUsername(ar.getUsername()).map((adminDetails) -> {
            if (passwordEncoder.matches(ar.getPassword(),adminDetails.getPassword())) {
                return ResponseEntity.ok(createAuthResponse(adminDetails));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private AuthResponse createAuthResponse(UserDetails userDetails){
        String tokenId = UUID.randomUUID().toString();
        String accessToken = jwtUtil.generateToken(userDetails, TokenType.ACCESS,tokenId);
        String refreshToken = jwtUtil.generateToken(userDetails,TokenType.REFRESH,tokenId);
        //校验成功后，将权限信息保存至redis中
        AdminDetails adminDetails = (AdminDetails) userDetails;
        savePermissionToRedis(adminDetails,tokenId);

        return new AuthResponse(userDetails.getUsername(),accessToken,refreshToken);
    }

    private void savePermissionToRedis(AdminDetails adminDetails,String tokenId){
        List<PermissionDomain> permissionDomainList = adminDetails.getRoleList().stream()
                .flatMap(role -> role.getPermList().stream()).collect(Collectors.toList());
        String redisKey = "perm_"+adminDetails.getUsername()+"_"+tokenId;
        permissionDomainRedisTemplate.opsForList().leftPushAll(redisKey,permissionDomainList);
        permissionDomainRedisTemplate.expire(redisKey,expirationRefreshTime, TimeUnit.SECONDS);
    }

    /**
     * 根据刷新token生成AccessToken信息
     * @param refreshToken 刷新令牌
     * @return 获取新的刷新令牌的相应
     */
    @RequestMapping(value = "/authorize/refreshToken",method = {RequestMethod.POST,RequestMethod.GET})
    public Mono<ResponseEntity<?>> refreshToken(@RequestParam(name = "refreshToken") @NonNull String refreshToken){
        String accessToken = jwtUtil.createAccessTokenByRefreshToken(refreshToken);
        if(accessToken == null){
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
        String username = jwtUtil.getUsernameFromToken(jwtUtil.getAllClaimsFromToken(refreshToken));
        return Mono.just(ResponseEntity.ok(new AuthResponse(username,accessToken,refreshToken)));
    }
}

