package com.dpacu.gateway.authorize.details.transfer.impl;


import com.dpacu.gateway.authorize.details.domain.AdminDomain;
import com.dpacu.gateway.authorize.details.domain.PermissionDomain;
import com.dpacu.gateway.authorize.details.domain.RoleDomain;
import com.dpacu.gateway.authorize.details.domain.status.AccountStatus;
import com.dpacu.gateway.authorize.details.domain.status.LoginType;
import com.dpacu.gateway.authorize.details.domain.status.RequestMethodType;
import com.dpacu.gateway.authorize.details.transfer.AuthorizeDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeDataTransferImpl implements AuthorizeDataTransfer {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public AdminDomain findByUsername(String username) {
        AdminDomain adminDomain = new AdminDomain("admin","123457");

        PermissionDomain permissionDomain = new PermissionDomain("perm1");
        permissionDomain.setPermChName("权限0");
        permissionDomain.setUriExpression("/admin/**");
        permissionDomain.setRequestMethodType(RequestMethodType.ALL);
        PermissionDomain permissionDomain1 = new PermissionDomain("perm1");
        permissionDomain1.setPermChName("权限1");
        permissionDomain1.setUriExpression("/user/**");
        permissionDomain1.setRequestMethodType(RequestMethodType.ALL);
        PermissionDomain permissionDomain2 = new PermissionDomain("perm1");
        permissionDomain2.setPermChName("权限2");
        permissionDomain2.setUriExpression("/guest/**");
        permissionDomain2.setRequestMethodType(RequestMethodType.ALL);
        List<PermissionDomain> permissionDomainList = new ArrayList<PermissionDomain>(){
            {
                add(permissionDomain);
                add(permissionDomain1);
                add(permissionDomain2);
            }
        };

        RoleDomain roleDomain = new RoleDomain("ADMIN");
        roleDomain.setRoleChName("管理员");
        roleDomain.setPermList(permissionDomainList);
        List<RoleDomain> roleDomainList = new ArrayList<RoleDomain>(){{
            add(roleDomain);
        }};

        adminDomain.setAccountStatus(AccountStatus.NORMAL);
        adminDomain.setLoginTime(LocalDateTime.now());
        adminDomain.setLoginType(LoginType.WEB);
        adminDomain.setRoleList(roleDomainList);
        adminDomain.setPassword(passwordEncoder.encode("123456"));
        return adminDomain;
    }
}
