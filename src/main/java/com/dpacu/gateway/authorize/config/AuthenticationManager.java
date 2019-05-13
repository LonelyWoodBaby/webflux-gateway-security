package com.dpacu.gateway.authorize.config;

import com.dpacu.gateway.authorize.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtUtil.getUsernameFromToken(jwtUtil.getAllClaimsFromToken(authToken));
        } catch (Exception e) {
            username = null;
        }
        Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
        if (username != null && jwtUtil.validateToken(authToken) && jwtUtil.validateAccessToken(claims)) {

            String roleName = (String) claims.get("role");
            List<String> roleNameList = new ArrayList<String>(){
                {
                    add(roleName);
                }
            };
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roleNameList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            return Mono.just(auth);
        }else{
            return Mono.empty();
        }
    }
}
