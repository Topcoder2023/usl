package com.manda.usl.manage.gui.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.manda.usl.http.server.entity.Result;
import com.manda.usl.kernel.annotation.Property;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.gui.entity.FileView;
import com.manda.usl.manage.gui.entity.LogView;
import com.manda.usl.manage.gui.mapper.FileMapper;
import com.manda.usl.manage.gui.mapper.LogMapper;
import org.slf4j.event.Level;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Singleton
public class FileService {
    @Property
    private LogMapper logMapper;

    @Property
    private FileMapper fileMapper;

    @Property("{manage.gui.categoryList}")
    private List<String> categoryList;

    public List<FileView> list(String name, String suffix) {
        return fileMapper.findFileByNameOrSuffix(name, suffix);
    }

    public void loadFileView() {
        if (CollUtil.isNotEmpty(categoryList)) {
            logMapper.insert(new LogView()
                    .setType(LogView.FILE_MANAGER)
                    .setTime(DateUtil.now())
                    .setLevel(Level.INFO.name())
                    .setMessage("扫描文件")
                    .setSource(this.getClass().getName()));

            List<FileView> toDeleteList = fileMapper.find(new FileView())
                    .stream()
                    .filter(view -> Objects.nonNull(view.getPath()))
                    .filter(view -> !FileUtil.exist(view.getPath()))
                    .toList();

            fileMapper.batchDelete(toDeleteList);
            categoryList.forEach(category -> this.loadFileView(category, any -> true));
        }
    }

    public void loadFileView(String categoryPath, FileFilter fileFilter) {
        FileUtil.loopFiles(categoryPath, fileFilter)
                .stream()
                .filter(FileUtil::isFile)
                .map(file -> new FileView()
                        .setName(file.getName())
                        .setSize(FileUtil.size(file))
                        .setCategory(file.getParent())
                        .setPath(file.getAbsolutePath())
                        .setSuffix(FileUtil.getSuffix(file))
                        .setModify(new DateTime(FileUtil.lastModifiedTime(file)).toString())
                        .generateUuid())
                .filter(view -> fileMapper.findOne(new FileView()
                        .setStatus(null)
                        .setUuid(view.getUuid())) == null)
                .forEach(fileMapper::insert);
    }

    public Result view(String uuid, Integer fileFlag) {
        String path = Optional.ofNullable(fileMapper.findOne(new FileView().setStatus(null).setUuid(uuid)))
                .map(fileView -> {
                    if (Objects.equals(1, fileFlag)) {
                        return fileView.getPath();
                    } else {
                        return fileView.getCategory();
                    }
                })
                .orElse(null);

        if (CharSequenceUtil.isBlank(path) || !FileUtil.exist(path)) {
            return Result.failure("无效路径，请重新加载");
        }

        DesktopUtil.open(new File(path));

        return Result.success();
    }

    public void clear() {
        logMapper.insert(new LogView()
                .setType(LogView.FILE_MANAGER)
                .setTime(DateUtil.now())
                .setLevel(Level.INFO.name())
                .setMessage("清除缓存")
                .setSource(this.getClass().getName()));

        fileMapper.deleteAll();
    }
}
