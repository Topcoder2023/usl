package com.manda.usl.manage.gui.entity;

import cn.hutool.crypto.digest.DigestUtil;
import com.manda.usl.data.sqlite.annotation.PrimaryKey;
import com.manda.usl.data.sqlite.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Table("FILE")
@Accessors(chain = true)
public class FileView {

    public static final String FIELD_NAME = "name";

    public static final String FIELD_SUFFIX = "suffix";

    @PrimaryKey
    private Long id;

    private Long size;

    private String uuid;

    private String name;

    private String path;

    private String suffix;

    private String category;

    private Integer status = 0;

    public FileView generateUuid() {
        this.uuid = DigestUtil.md5Hex(path);
        return this;
    }
}
