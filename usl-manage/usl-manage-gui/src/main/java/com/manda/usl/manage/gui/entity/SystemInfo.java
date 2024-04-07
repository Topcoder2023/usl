package com.manda.usl.manage.gui.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hongda.li
 */
@Data
@NoArgsConstructor
public class SystemInfo {

    private String description;

    private String value;

    public SystemInfo(String description, Object value) {
        this.description = description;
        this.value = String.valueOf(value);
    }
}
