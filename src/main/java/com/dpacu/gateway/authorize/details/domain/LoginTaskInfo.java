package com.dpacu.gateway.authorize.details.domain;

import lombok.Data;

@Data
public class LoginTaskInfo {
    private String username;
    private long loginTime;
    private boolean loginSuccess;
    private String failureReason;

    public static LoginTaskInfo failure(String username,String failureReason){
        LoginTaskInfo loginTaskInfo = new LoginTaskInfo();
        loginTaskInfo.setLoginTime(System.currentTimeMillis());
        loginTaskInfo.setUsername(username);
        loginTaskInfo.setLoginSuccess(false);
        loginTaskInfo.setFailureReason(failureReason);
        return loginTaskInfo;
    }
}
