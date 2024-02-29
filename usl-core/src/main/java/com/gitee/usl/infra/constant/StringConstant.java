package com.gitee.usl.infra.constant;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;

/**
 * @author hongda.li
 */
public final class StringConstant {

    private StringConstant() {
    }

    public static final String VERSION = "v1.0.0.release";

    public static final String USL_RUNNER_NAME_PREFIX = "USL Runner-";

    public static final String FILE_SPLIT = SystemUtil.get(SystemUtil.FILE_SEPARATOR);

    public static final String DEFAULT_PATH = FileUtil.getUserHomePath() + FILE_SPLIT;

    public static final String FIRST_USL_RUNNER_NAME = "USL Runner-1";

    public static final String COMPILED_SCRIPT = "compiledScript";

    public static final String PARAMS_NAME = "paramsName";

    public static final String SCRIPT_NAME = "script";

    public static final String RUNNER_NAME = "runner";

    public static final String USL_CLI_PREFIX = "USL > ";

    public static final String USL_NAME = "USL";

    public static final String CONTENT_TYPE_SUFFIX = "; charset=utf-8";

    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    public static final String PASSWORD = "password";

    public static final String SCRIPT_SUFFIX = "js";

    public static final String RESET_LEVEL_METHOD_NAME = "resetLevel";

    public static final String ADD_LOGGER_FILTER_METHOD_NAME = "addFilter";
}
