package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FuncClient {
    /**
     * 指定调用的函数服务器的前缀
     * <br/>
     * Schema + Domain + Port
     * 例如：http://127.0.0.1:10001
     * 或指定调用服务的名称
     * 且服务基本信息需要注册到远程调用配置注册表中
     * 例如："remoteService"
     * <br/
     * 当声明多个服务实现时
     * 会自动根据负载均衡机制选择其中一个服务作为调用目标
     * 默认的负载均衡机制时轮询
     * 也可以自定义负载均衡机制以实现有权重或随机调用
     *
     * @return 函数客户端调用的目标服务
     */
    @SuppressWarnings("JavadocLinkAsPlainText") String[] value();
}
