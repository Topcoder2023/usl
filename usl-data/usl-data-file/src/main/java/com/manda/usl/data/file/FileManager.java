package com.manda.usl.data.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.google.auto.service.AutoService;
import com.manda.usl.data.file.entity.FileView;
import com.manda.usl.data.file.mapper.FileMapper;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.common.BeanLoader;
import com.manda.usl.kernel.common.SingleBeanLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton
@AutoService(BeanLoader.class)
public class FileManager implements SingleBeanLoader {

    private static FileMapper fileMapper;

    public static void scan(String categoryPath, FileFilter fileFilter) {
        List<File> fileList = FileUtil.loopFiles(categoryPath, fileFilter)
                .stream()
                .filter(FileUtil::isFile)
                .toList();
        if (CollUtil.isEmpty(fileList)) {
            return;
        }

        FileMapper mapper = getFileMapper();

        for (File file : fileList) {
            FileView view = new FileView()
                    .setName(file.getName())
                    .setCategory(file.getParent());

            FileView exists = mapper.findOne(view);
            if (exists == null) {
                mapper.insert(view
                        .setSize(FileUtil.size(file))
                        .setPath(file.getAbsolutePath())
                        .setSuffix(FileUtil.getSuffix(file)));
            }
        }
    }

    @Override
    public Class<?> singleton() {
        return FileManager.class;
    }

    public static FileMapper getFileMapper() {
        if (fileMapper == null) {
            fileMapper = BeanContext.findFirst(FileMapper.class);
        }
        return fileMapper;
    }
}
