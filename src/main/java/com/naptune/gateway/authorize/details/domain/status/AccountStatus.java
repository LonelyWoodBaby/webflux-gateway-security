package com.naptune.gateway.authorize.details.domain.status;

public enum AccountStatus {
    /**
     * 初始化
     */
    INITIAL,
    /**
     * 正常
     */
    NORMAL,
    /**
     * 被锁定
     */
    LOCKED,
    /**
     * 被封禁
     */
    BANNED,
    /**
     * 不可用
     */
    DISABLE
}
