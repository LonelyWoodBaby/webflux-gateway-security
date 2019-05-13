package com.dpacu.gateway.authorize.details.domain;

import com.dpacu.gateway.authorize.details.domain.status.AccountStatus;
import com.dpacu.gateway.authorize.details.domain.status.LoginType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminDomain implements Serializable {
    /**
     * 用户名，且唯一，用于用户业务ID
     */
    private final String username;
    private String password;
    private AccountStatus accountStatus;
    private LocalDateTime loginTime;
    private LoginType loginType;

    private List<RoleDomain> roleList;

    public AdminDomain(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountStatus = AccountStatus.NORMAL;
    }
}
