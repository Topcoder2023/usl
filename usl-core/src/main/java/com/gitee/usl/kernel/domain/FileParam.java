package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLNotFoundException;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

/**
 * @author hongda.li
 */
@Getter
@ToString
@Description("文件参数，用以快速加载文件中的文本内容作为脚本")
public class FileParam extends Param {

    @Description("错误消息")
    private static final String NOT_FOUND = "无法读取此路径下的文件 - [{}]";

    @Description("文件对象")
    private final File file;

    public FileParam(File file) {
        this.file = file;
        init();
    }

    public FileParam(String path) {
        this(new File(path));
    }

    private void init() {
        Assert.isTrue(this.file.exists(), () -> new USLNotFoundException(NOT_FOUND, file.getAbsolutePath()));
        this.setScript(new FileReader(this.file).readString());
    }

}
