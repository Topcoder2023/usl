package com.gitee.usl.function;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@FunctionGroup
public class FileFunction {

    @Function("file")
    public File file(String path) {
        return new File(path);
    }

    @Function("file_exists")
    public boolean exists(String path) {
        return FileUtil.exist(path);
    }

    @Function("file_delete")
    public boolean delete(String path) {
        return FileUtil.del(path);
    }

    @Function("file_is_directory")
    public boolean isDirectory(String path) {
        return FileUtil.isDirectory(path);
    }

    @Function("file_list")
    public List<File> list(String path) {
        return isDirectory(path) ? ListUtil.toList(FileUtil.ls(path)) : Collections.emptyList();
    }

    @Function("file_list_loop")
    public List<File> loop(String path) {
        return FileUtil.loopFiles(path);
    }

}
