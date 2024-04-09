package com.manda.usl.manage.entity;

import cn.hutool.crypto.digest.DigestUtil;
import com.manda.usl.data.sql.annotation.PrimaryKey;
import com.manda.usl.data.sql.annotation.Table;
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

    public static final String FIELD_MODIFY = "modify";

    @PrimaryKey
    private Long id;

    private Long size;

    private String uuid;

    private String name;

    private String path;

    private String suffix;

    private String category;

    private String modify;

    private Integer status = 0;

    public FileView generateUuid() {
        this.uuid = DigestUtil.md5Hex(path + modify);
        return this;
    }
}
