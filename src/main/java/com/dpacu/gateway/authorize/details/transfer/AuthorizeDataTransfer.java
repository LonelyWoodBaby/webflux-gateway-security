package com.dpacu.gateway.authorize.details.transfer;


import com.dpacu.gateway.authorize.details.domain.AdminDomain;

public interface AuthorizeDataTransfer {
    public AdminDomain findByUsername(String username);
}
