package com.gitee.usl.infra.database;

import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.infra.proxy.MethodInterceptor;

import java.sql.Connection;

/**
 * @author hongda.li
 */
public class UslDbConnection extends MethodInterceptor<Connection> {
    protected UslDbConnection(Connection target) {
        super(target, Connection.class);
    }

    @Override
    protected Object intercept(Invocation<Connection> invocation, Object proxy) {

        return null;
    }
}
