package com.dpacu.gateway.authorize.details.service;

import com.dpacu.gateway.authorize.details.AdminDetails;
import com.dpacu.gateway.authorize.details.domain.AdminDomain;
import com.dpacu.gateway.authorize.details.transfer.AuthorizeDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReactAdminDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private AuthorizeDataTransfer authorizeDataTransfer;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        System.out.println("username=" + username);
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        AdminDomain adminDomain = authorizeDataTransfer.findByUsername(username);
        AdminDetails admin = new AdminDetails(adminDomain.getUsername(),adminDomain.getPassword() ,adminDomain.getRoleList(), list);
        return Mono.just(admin);
    }
}
