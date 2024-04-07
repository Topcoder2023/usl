package com.manda.usl.manage.gui.service;

import com.manda.usl.data.file.FileManager;
import com.manda.usl.data.file.entity.FileView;
import com.manda.usl.data.file.mapper.FileMapper;

import java.util.List;

/**
 * @author hongda.li
 */
public class FileService {

    public List<FileView> list(String name, String suffix) {
        FileMapper mapper = FileManager.getFileMapper();
        return null;
    }
}
