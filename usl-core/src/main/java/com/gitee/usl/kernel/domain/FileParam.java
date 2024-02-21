package com.gitee.usl.kernel.domain;

import cn.hutool.core.io.file.FileReader;
import com.gitee.usl.api.annotation.Description;
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

    @Description("文件对象")
    private final File file;

    public FileParam(File file) {
        super(new FileReader(file).readString());
        this.file = file;
    }

    public FileParam(String path) {
        this(new File(path));
    }

}
