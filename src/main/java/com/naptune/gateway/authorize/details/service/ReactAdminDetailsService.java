package com.naptune.gateway.authorize.details.service;

import com.naptune.gateway.authorize.details.AdminDetails;
import com.naptune.gateway.authorize.details.domain.AdminDomain;
import com.naptune.gateway.authorize.details.transfer.AuthorizeDataTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReactAdminDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private AuthorizeDataTransfer authorizeDataTransfer;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("本次登录用户：" + username);
//        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
//        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        AdminDomain adminDomain = authorizeDataTransfer.findByUsername(username);
        List<GrantedAuthority> list = adminDomain.getRoleList().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
                .collect(Collectors.toList());
        AdminDetails admin = new AdminDetails(adminDomain.getUsername(),adminDomain.getPassword() ,adminDomain.getRoleList(), list);
        return Mono.just(admin);
    }

}
