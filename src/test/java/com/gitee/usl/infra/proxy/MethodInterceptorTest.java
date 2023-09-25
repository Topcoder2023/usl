package com.gitee.usl.infra.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 动态代理测试
 *
 * @author hongda.li
 */
class MethodInterceptorTest {

    @Test
    void createProxy() {

        Assertions.assertEquals("success",
                new MethodInterceptor<>(DemoTest.class) {
                    @Override
                    protected Object intercept(Invocation<DemoTest> uslInvocation, Object proxy) {
                        System.out.println("proxy method for interface");
                        return "success";
                    }
                }.createProxy().method());

        Assertions.assertEquals("success",
                new MethodInterceptor<>(new DemoClassTest(), DemoTest.class) {
                    @Override
                    protected Object intercept(Invocation<DemoTest> uslInvocation, Object proxy) {
                        System.out.println("proxy method for object");
                        return "success";
                    }
                }.createProxy().method());
    }

    public interface DemoTest {

        String method();
    }

    public static class DemoClassTest implements DemoTest {

        @Override
        public String method() {
            return "failure";
        }
    }
}