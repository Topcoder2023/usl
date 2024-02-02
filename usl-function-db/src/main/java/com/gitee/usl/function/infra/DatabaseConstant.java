package com.gitee.usl.function.infra;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;

/**
 * @author hongda.li
 */
public class DatabaseConstant {
    private DatabaseConstant () {}

    public static final String FILE_SPLIT = SystemUtil.get(SystemUtil.FILE_SEPARATOR);

    public static final String DEFAULT_PATH = FileUtil.getUserHomePath() + FILE_SPLIT;

    public static final String DATABASE_SUFFIX = ".db";

    public static final String SQLITE_URL_PREFIX = "jdbc:sqlite:";
}
