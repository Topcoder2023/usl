package com.manda.usl.manage.gui.entity;

import cn.hutool.core.text.CharSequenceUtil;
import com.manda.usl.data.sqlite.annotation.PrimaryKey;
import com.manda.usl.data.sqlite.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Table("LOG")
@Accessors(chain = true)
public class LogView {

    public static final String FILE_MANAGER = "文件管理";

    public static final String FIELD_TIME = "time";

    public static final String FIELD_TYPE = "type";

    public static final String FIELD_LEVEL = "level";

    @PrimaryKey
    private Long id;

    private String time;

    private String type;

    private String level;

    private String message;

    private String source;

    public LogView setMessage(String message) {
        this.message = message;
        return this;
    }

    public LogView setMessage(String message, Object... arguments) {
        this.message = CharSequenceUtil.format(message, arguments);
        return this;
    }
}
