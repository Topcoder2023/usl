package com.manda.usl.manage.gui.entity;

import cn.hutool.core.io.FileUtil;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@Data
public class GuiConfiguration {

    private List<String> categoryList = Collections.singletonList(FileUtil.getTmpDirPath());
}
