package com.naptune.gateway.authorize.details.domain;

import com.naptune.gateway.authorize.details.domain.status.RequestMethodType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PermissionDomain implements Serializable {
    public PermissionDomain() {
    }

    public PermissionDomain(String permName) {
        this.permName = permName;
    }

    private String permName;
    private String permChName;
    private String uriExpression;
    private RequestMethodType requestMethodType;
    private List<RoleDomain> roleList;
}