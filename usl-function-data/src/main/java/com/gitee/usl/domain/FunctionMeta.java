package com.gitee.usl.domain;

import cn.hutool.db.Entity;

import static com.gitee.usl.infra.DatabaseConstant.TABLE_FUNCTION_NAME;

/**
 * @author hongda.li
 */
public class FunctionMeta {
    public static final String UNKNOWN = "UNKNOWN";

    public static final String ID = "ID";

    public static final String RUNNER_NAME = "RUNNER_NAME";

    public static final String FUNCTION_NAME = "FUNCTION_NAME";

    public static final String ALIAS_NAME = "ALIAS_NAME";

    public static final String ATTRIBUTE = "ATTRIBUTE";

    public static final String CLASS_NAME = "CLASS_NAME";

    public static final String METHOD_NAME = "METHOD_NAME";

    public static final String PLUGIN_NAME = "PLUGIN_NAME";

    public static Entity byId(Entity entity) {
        return Entity.create().set(ID, entity.get(ID));
    }

    public static Entity selectByName(String runnerName,
                                      String functionName) {
        return Selector.of(TABLE_FUNCTION_NAME)
                .select(FunctionMeta.ID)
                .andEquals(FunctionMeta.RUNNER_NAME, runnerName)
                .andEquals(FunctionMeta.FUNCTION_NAME, functionName)
                .findOne();
    }

    public static Entity create(String runnerName,
                                String functionName,
                                String className) {
        return Entity.create(TABLE_FUNCTION_NAME)
                .set(RUNNER_NAME, runnerName)
                .set(FUNCTION_NAME, functionName)
                .set(CLASS_NAME, className);
    }

    public static Entity create(String runnerName,
                                String functionName,
                                String aliasName,
                                String attribute,
                                String className,
                                String methodName,
                                String pluginName) {
        return Entity.create(TABLE_FUNCTION_NAME)
                .set(RUNNER_NAME, runnerName)
                .set(FUNCTION_NAME, functionName)
                .set(ALIAS_NAME, aliasName)
                .set(ATTRIBUTE, attribute)
                .set(CLASS_NAME, className)
                .set(METHOD_NAME, methodName)
                .set(PLUGIN_NAME, pluginName);
    }
}
