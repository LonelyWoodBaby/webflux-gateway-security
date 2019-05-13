package com.dpacu.gateway.authorize.details.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDomain implements Serializable {
    private final String roleName;
    private String roleChName;

    private List<AdminDomain> adminList;
    private List<PermissionDomain> permList;
}
