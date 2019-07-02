package com.naptune.gateway.authorize.details.transfer;


import com.naptune.gateway.authorize.details.domain.AdminDomain;

public interface AuthorizeDataTransfer {
    public AdminDomain findByUsername(String username);
}
