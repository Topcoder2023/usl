package com.gitee.usl.function;

import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.io.File;

/**
 * @author hongda.li
 */
@FunctionGroup
public class FileFunction {

    @Function("file")
    public File file(String path) {
        return new File(path);
    }
}
