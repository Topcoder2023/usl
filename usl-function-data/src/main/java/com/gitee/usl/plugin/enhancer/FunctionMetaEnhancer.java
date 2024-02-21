package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.domain.FunctionMeta;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.structure.wrapper.BoolWrapper;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.impl.FunctionMetaPlugin;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gitee.usl.domain.FunctionMeta.UNKNOWN;
import static com.gitee.usl.infra.DatabaseConstant.*;

/**
 * 函数元数据增强器
 *
 * @author hongda.li
 */
@Slf4j
public class FunctionMetaEnhancer extends AbstractFunctionEnhancer {
    private final BoolWrapper init = new BoolWrapper(false);

    @Override
    protected void enhancePluggable(FunctionPluggable fp) {
        fp.plugins().install(new FunctionMetaPlugin());
    }

    @Override
    protected void enhanceFunction(_Function func) {
        try {
            this.write2Db(func);
        } catch (SQLException e) {
            log.error("新增函数元数据信息异常", e);
            throw new USLException(e);
        }
    }

    private void initTable() {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.execute(ResourceUtil.readUtf8Str(TABLE_FUNCTION_SQL));
            proxy.execute(ResourceUtil.readUtf8Str(TABLE_INVOKE_SQL));
            init.set(true);
        } catch (SQLException e) {
            log.error("[函数元数据表结构 / 函数调用表结构] 初始化失败", e);
            throw new USLException(e);
        }
    }

    private void write2Db(_Function func) throws SQLException {
        if (init.isFalse()) {
            this.initTable();
        }

        Db proxy = ConnectHelper.connectSystem().proxy();

        if (func instanceof Definable definable) {
            FunctionDefinition definition = definable.definition();
            String runnerName = definition.getRunner().getName();
            String functionName = definition.getName();
            String attribute = JSONUtil.toJsonStr(definition.getAttribute());
            String className = definition.getMethodMeta().targetType().getName();
            String aliasName = JSONUtil.toJsonStr(definition.getAlias()
                    .stream()
                    .filter(name -> !Objects.equals(name, func.name()))
                    .collect(Collectors.toSet()));
            String methodName = Optional.ofNullable(definition.getMethodMeta().method())
                    .map(Method::getName)
                    .orElse(null);
            String pluginName = func instanceof FunctionPluggable fp
                    ? JSONUtil.toJsonStr(fp.plugins()
                    .getContainer()
                    .stream()
                    .map(Plugin::getClass)
                    .map(Class::getName)
                    .collect(Collectors.toList()))
                    : JSONUtil.toJsonStr(Collections.emptyList());

            // 已存在的数据
            Entity exists = FunctionMeta.selectByName(runnerName, functionName);
            // 待插入的数据
            Entity insert = FunctionMeta.create(runnerName,
                    functionName,
                    aliasName,
                    attribute,
                    className,
                    methodName,
                    pluginName);

            if (exists == null) {
                proxy.insert(insert);
            } else {
                proxy.update(insert, FunctionMeta.byId(exists));
            }
        } else {
            // 已存在的数据
            Entity exists = FunctionMeta.selectByName(UNKNOWN, func.name());
            // 待插入的数据
            Entity insert = FunctionMeta.create(UNKNOWN, func.name(), func.getClass().getName());

            if (exists == null) {
                proxy.insert(insert);
            } else {
                proxy.update(insert, FunctionMeta.byId(exists));
            }

        }
    }
}
