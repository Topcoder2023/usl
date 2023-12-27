package com.gitee.usl.function;

import cn.hutool.core.swing.DesktopUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.io.File;

/**
 * @author hongda.li
 */
@FunctionGroup
public class SystemFunction {

    @Function("system_open")
    public File open(File file) {
        DesktopUtil.open(file);
        return file;
    }

    @Function("system_open_path")
    public File open(String path) {
        return this.open(new File(path));
    }

    @Function("system_browse")
    public String browse(String url) {
        DesktopUtil.browse(url);
        return url;
    }
}
