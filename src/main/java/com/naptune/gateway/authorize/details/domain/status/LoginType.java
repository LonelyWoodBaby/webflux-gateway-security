package com.naptune.gateway.authorize.details.domain.status;

public enum LoginType {
    WEB("web"),
    MOBILE("mobile");

    private String loginType;
    LoginType(String loginType) {
        this.loginType = loginType;
    }

    public static LoginType defaultType(){
        return LoginType.WEB;
    }
}
