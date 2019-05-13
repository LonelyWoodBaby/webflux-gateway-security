package com.dpacu.gateway.authorize.details.domain;

import com.dpacu.gateway.authorize.details.domain.status.RequestMethodType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PermissionDomain implements Serializable {
    private final String permName;
    private String permChName;
    private String uriExpression;
    private RequestMethodType requestMethodType;
    private List<RoleDomain> roleList;
}