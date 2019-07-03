package com.naptune.gateway.authorize.details.transfer.impl;


import com.naptune.gateway.authorize.details.client.AdminFeignClient;
import com.naptune.gateway.authorize.details.domain.AdminDomain;
import com.naptune.gateway.authorize.details.domain.PermissionDomain;
import com.naptune.gateway.authorize.details.domain.RoleDomain;
import com.naptune.gateway.authorize.details.domain.status.AccountStatus;
import com.naptune.gateway.authorize.details.domain.status.LoginType;
import com.naptune.gateway.authorize.details.domain.status.RequestMethodType;
import com.naptune.gateway.authorize.details.transfer.AuthorizeDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizeDataTransferImpl implements AuthorizeDataTransfer {
    @Autowired
    private AdminDomain defaultManagerDomain;
    @Autowired
    private List<String> defaultManagerList;
    private AdminFeignClient adminFeignClient;
    @Override
    public AdminDomain findByUsername(String username) {
        if(defaultManagerList != null && defaultManagerList.contains(username)){
            return findDefaultAdmin(username);
        }
        return adminFeignClient.findAdminByUsername(username);
    }

    private AdminDomain findDefaultAdmin(String username){
        return defaultManagerDomain;
    }

}
