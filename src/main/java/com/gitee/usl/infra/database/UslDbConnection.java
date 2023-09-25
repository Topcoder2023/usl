package com.gitee.usl.infra.database;

import com.dbmasker.api.DBManager;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.gitee.usl.infra.proxy.UslMethodInterceptor;

import java.sql.Connection;

/**
 * @author hongda.li
 */
public class UslDbConnection extends UslMethodInterceptor<Connection> {
    protected UslDbConnection(Connection target) {
        super(target, Connection.class);
    }

    @Override
    protected Object intercept(UslInvocation<Connection> uslInvocation, Object proxy) {

        return null;
    }
}
