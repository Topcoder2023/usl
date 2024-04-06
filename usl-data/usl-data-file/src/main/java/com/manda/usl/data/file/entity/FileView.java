package com.manda.usl.data.file.entity;

import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.http.HttpUtil;
import com.manda.usl.data.sqlite.annotation.PrimaryKey;
import com.manda.usl.data.sqlite.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.Objects;

/**
 * @author hongda.li
 */
@Data
@Table("FILE")
@Accessors(chain = true)
public class FileView {

    @PrimaryKey
    private Long id;

    private Long size;

    private String name;

    private String path;

    private String suffix;

    private String category;

    public FileView openFile() {
        Objects.requireNonNull(this.path);
        if (HttpUtil.isHttp(this.path) || HttpUtil.isHttps(this.path)) {
            DesktopUtil.browse(this.path);
        } else {
            DesktopUtil.open(new File(this.path));
        }
        return this;
    }

    public FileView openCategory() {
        Objects.requireNonNull(this.category);
        DesktopUtil.open(new File(this.category));
        return this;
    }
}
