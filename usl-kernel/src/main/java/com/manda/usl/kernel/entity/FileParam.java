package com.manda.usl.kernel.entity;

import cn.hutool.core.io.file.FileReader;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class FileParam extends Param {

    private final File file;

    public FileParam(File file) {
        super(new FileReader(file).readString());
        this.file = file;
    }

    public FileParam(String path) {
        this(new File(path));
    }

}
