package com.manda.usl.manage.gui.api;

import com.manda.usl.data.file.entity.FileView;
import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.kernel.annotation.Param;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.gui.service.FileService;

import java.util.List;

/**
 * @author hongda.li
 */
@Singleton
@Mapping("/file")
public class FileApi {

    private final FileService fileService;

    public FileApi(FileService fileService) {
        this.fileService = fileService;
    }

    @Mapping("/list")
    public List<FileView> list(@Param("name") String name,
                               @Param("suffix") String suffix) {
        return fileService.list(name, suffix);
    }
}
