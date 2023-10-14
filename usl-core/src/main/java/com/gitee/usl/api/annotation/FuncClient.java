package com.gitee.usl.api.annotation;

import com.gitee.usl.infra.constant.StringConstant;

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
    @SuppressWarnings("JavadocLinkAsPlainText") String name();

    /**
     * 调用的脚本内容
     *
     * @return 脚本内容
     */
    String script();

    /**
     * 函数名称
     * 默认取方法或类的名称并将首字母小写
     * 支持指定多个名称
     *
     * @return 函数名称数组
     */
    String[] value() default {};

    /**
     * 指定 HTTP 客户端所使用的配置
     * 因客户端配置与执行器绑定
     * 因此仅需返回执行器的名称
     *
     * @return 执行器的名称
     */
    String runnerName() default StringConstant.FIRST_USL_RUNNER_NAME;
}
