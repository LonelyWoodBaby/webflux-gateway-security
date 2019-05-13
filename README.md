首先感谢[spring-boot-webflux-jjwt](https://github.com/ard333/spring-boot-webflux-jjwt)开源项目的支持

# 网关&授权模块说明

## 使用技术框架：

- JDK版本：1.8.131
- Springboot版本：2.1.4
- SpringCloud版本：Greenwich.SR1

- 授权模块：Spring Security

- 注册中心： Eureka

- 令牌机制： JWT
- 网关技术：Spring Cloud Gateway

## 已实现功能：
- 基于webflux开发
- 加入spring cloud gateway网关
- 加入服务调用时间统计
- 加入Hysrix熔断功能
- 加入ribbon负载均衡功能
- 加入eureka网关注册功能（没有集成server端，默认关闭）
- JWT令牌生成
- 刷新令牌功能
- 基于JWT的鉴权功能
- rbac(user-role-permission)模式的用户角色鉴权功能
- 权限信息将保存至redis中（需要redis中间件）


## 尚未完成事项：
- 缺少gateway限流操作（与redis相结合）


