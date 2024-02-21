package com.gitee.usl.function.db;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.db.Db;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.function.infra.ConnectHelper;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.plugin.annotation.NotNull;

import static com.gitee.usl.function.infra.DatabaseConstant.*;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ConnectFunction {

    @Function("db_connect")
    public Database connect(@NotNull String databaseName) {
        String path = DEFAULT_PATH + StringConstant.USL_NAME + FILE_SPLIT;
        return this.connectPath(path, databaseName);
    }

    @Function("db_connect_path")
    public Database connectPath(@NotNull String pathName, @NotNull String databaseName) {
        String path = CharSequenceUtil.addSuffixIfNot(pathName, FILE_SPLIT);
        String name = CharSequenceUtil.addSuffixIfNot(databaseName, DATABASE_SUFFIX);
        return ConnectHelper.newDatabase(path, name);
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
