package com.manda.usl.data.file.api;

import com.manda.usl.data.file.entity.FileView;
import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.kernel.annotation.Singleton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@Mapping("/file")
@Singleton
public class FileApi {

    @Mapping("/list")
    public List<FileView> list() {
        return Collections.emptyList();
    }
}
