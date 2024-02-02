package com.gitee.usl.function.db;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.domain.Database;
import com.gitee.usl.function.infra.ConnectHelper;
import com.gitee.usl.infra.structure.SharedSession;

import static com.gitee.usl.function.infra.DatabaseConstant.*;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ConnectFunction {

    @Function("db_connect")
    public Database connect(String databaseName) {
        String runnerName = SharedSession.getSession().getDefinition().getRunner().getName();
        String path = DEFAULT_PATH + runnerName + FILE_SPLIT;
        String name = CharSequenceUtil.addSuffixIfNot(databaseName, DATABASE_SUFFIX);
        return ConnectHelper.newDatabase(path, name);
    }

}
