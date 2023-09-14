package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.exception.UslNotFoundException;

import java.io.File;

/**
 * 文件参数
 * 用以快速加载文件中的文本内容作为脚本
 *
 * @author hongda.li
 */
public class UslFileParam extends UslParam {
    private static final String NOT_FOUND = "File not found in path [{}]";
    private final File file;

    public UslFileParam(File file) {
        this.file = file;
        init();
    }

    public UslFileParam(String path) {
        this(new File(path));
    }

    private void init() {
        Assert.isTrue(this.file.exists(), () -> new UslNotFoundException(NOT_FOUND, file.getAbsolutePath()));

        FileReader reader = new FileReader(this.file);
        this.setContent(reader.readString());
    }

    public File getFile() {
        return file;
    }
}
