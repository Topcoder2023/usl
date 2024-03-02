package com.gitee.usl.function.data;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.db.Db;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.Database;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.plugin.annotation.NotNull;

import static com.gitee.usl.infra.DatabaseConstant.*;
import static com.gitee.usl.infra.constant.StringConstant.DEFAULT_PATH;
import static com.gitee.usl.infra.constant.StringConstant.FILE_SPLIT;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ConnectFunction {

    @Function("db_connect")
    public Database connect(@NotNull String databaseName) {
        return ConnectHelper.connect(databaseName);
    }

    @Function("db_connect_path")
    public Database connectPath(@NotNull String pathName, @NotNull String databaseName) {
        return ConnectHelper.connect(pathName, databaseName);
    }

    @Function("db_clear")
    public void clear(@NotNull String databaseName) {
        String runnerName = SharedSession.getSession().getDefinition().getRunner().getName();
        String path = DEFAULT_PATH + runnerName + FILE_SPLIT;
        this.clearPath(path, databaseName);
    }

    @Function("db_clear_path")
    public void clearPath(@NotNull String pathName, @NotNull String databaseName) {
        String path = CharSequenceUtil.addSuffixIfNot(pathName, FILE_SPLIT);
        String name = CharSequenceUtil.addSuffixIfNot(databaseName, DATABASE_SUFFIX);
        FileUtil.del(path + name);
    }

    @Function("db_proxy")
    public Db proxy(@NotNull Database database) {
        return database.proxy();
    }

    @Function("db_path")
    public String path(@NotNull Database database) {
        return database.path();
    }
}
