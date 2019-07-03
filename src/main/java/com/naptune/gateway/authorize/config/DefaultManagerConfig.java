package com.naptune.gateway.authorize.config;

import com.naptune.gateway.authorize.details.domain.AdminDomain;
import com.naptune.gateway.authorize.details.domain.PermissionDomain;
import com.naptune.gateway.authorize.details.domain.RoleDomain;
import com.naptune.gateway.authorize.details.domain.status.AccountStatus;
import com.naptune.gateway.authorize.details.domain.status.LoginType;
import com.naptune.gateway.authorize.details.domain.status.RequestMethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李亚斌
 * @date 2019/07/03 18:29
 * @since v1.1
 */
@Configuration
public class DefaultManagerConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public List<String> defaultManagerList (){
        List<String> defaultUserList = new ArrayList<String>() {
            {
                add("xiaoweixintuo");
            }
        };
        return defaultUserList;
    }

    @Bean
    public AdminDomain defaultManagerDomain(){
        //仅限小微信托使用
        AdminDomain adminDomain = new AdminDomain("xiaoweixintuo","123457");

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
