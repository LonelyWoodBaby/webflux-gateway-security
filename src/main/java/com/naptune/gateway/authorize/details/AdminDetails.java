package com.naptune.gateway.authorize.details;

import com.naptune.gateway.authorize.details.domain.RoleDomain;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class AdminDetails extends User {
    private List<RoleDomain> roleList;

    public AdminDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public AdminDetails(String username, String password, List<RoleDomain> roleList, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.roleList = roleList;
    }
}
