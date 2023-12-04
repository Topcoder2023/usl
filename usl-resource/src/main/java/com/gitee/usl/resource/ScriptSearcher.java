package com.gitee.usl.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.api.WebHelper;
import com.gitee.usl.resource.entity.ScriptInfo;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class ScriptSearcher {
    private ScriptSearcher() {
    }

    public static int findCount() {
        return findAll().size();
    }

    public static ScriptInfo findOne(String scriptName, String belongs) {
        return findAllToInfo(scriptName, belongs, true)
                .stream()
                .findFirst()
                .map(info -> info.setContent(new FileReader(info.getPath()).readString()))
                .orElse(null);
    }

    public static List<ScriptInfo> findAllToInfo(String scriptName, String belongs) {
        return findAllToInfo(scriptName, belongs, false);
    }

    public static List<ScriptInfo> findAllToInfo(String scriptName, String belongs, boolean showPath) {
        return findAll().stream()
                .map(file -> {
                    String name = FileUtil.getName(file);
                    List<String> info = CharSequenceUtil.split(name, StrPool.AT);
                    return new ScriptInfo()
                            .setPath(showPath ? file.getAbsolutePath() : null)
                            .setBelongs(info.get(0))
                            .setScriptName(info.get(1).substring(0, info.get(1).lastIndexOf(StrPool.DOT)))
                            .setLastUpdatedTime(DateUtil.formatDateTime(new Date(file.lastModified())))
                            .setFileSize(FileUtil.size(file));
                })
                .filter(info -> CharSequenceUtil.isBlank(scriptName) || CharSequenceUtil.contains(info.getScriptName(), scriptName))
                .filter(info -> CharSequenceUtil.isBlank(belongs) || CharSequenceUtil.contains(info.getBelongs(), belongs))
                .collect(Collectors.toList());
    }

    public static List<File> findAll() {
        USLRunner runner = WebHelper.RUNNER_THREAD_LOCAL.get();
        String directory = runner.configuration()
                .configEngine()
                .getScriptPath()
                + runner.name();
        FileUtil.mkdir(directory);
        return Arrays.stream(FileUtil.ls(directory))
                .filter(file -> Objects.equals(FileUtil.getSuffix(file), StringConstant.SCRIPT_SUFFIX))
                .collect(Collectors.toList());
    }

    public static String buildScriptPath(USLRunner runner, ScriptInfo scriptInfo) {
        return runner.configuration()
                .configEngine()
                .getScriptPath()
                + runner.name()
                + StrPool.SLASH
                + scriptInfo.buildName()
                + StrPool.DOT
                + StringConstant.SCRIPT_SUFFIX;
    }
}
