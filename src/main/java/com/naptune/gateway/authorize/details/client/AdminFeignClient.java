package com.naptune.gateway.authorize.details.client;

import com.naptune.gateway.authorize.details.domain.AdminDomain;

/**
 * @author 李亚斌
 * @date 2019/07/03 18:20
 * @since v1.1
 */
public interface AdminFeignClient {
    /**
     * 根据username查询用户信息，至少应该包括密码，角色，权限相关信息。
     * @param username 用户名
     * @return 完整的用户信息
     */
    public AdminDomain findAdminByUsername(String username);
}
