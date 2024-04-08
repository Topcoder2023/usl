package com.manda.usl.manage.gui.entity;

import com.manda.usl.data.sqlite.annotation.PrimaryKey;
import com.manda.usl.data.sqlite.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hongda.li
 */
@Data
@Table("LOG")
@Accessors(chain = true)
public class LogView {

    @PrimaryKey
    private Long id;

    private Date time;

    private String type;

    private String level;

    private String message;
}
