package com.gitee.usl.kernel.configure;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.VariableDefinable;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.kernel.engine.ScriptEngineInitializer;
import com.gitee.usl.api.FunctionMissing;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class EngineConfig {

    @Description("脚本创建路径")
    private String scriptPath = FileUtil.getUserHomePath();

    @Description("是否开启调试")
    private Boolean enableDebug = Boolean.FALSE;

    @Description("是否开启对象方法调用")
    private Boolean enableMethodInvoke = Boolean.TRUE;

    @Description("函数访问兜底机制")
    private FunctionMissing functionMissing;

    @Description("变量初始化器")
    private VariableDefinable varInitializer;

    @Description("USL配置类")
    private final Configuration configuration;

    @Description("包扫描路径")
    private final Set<String> packageNameList;

    @Description("函数实例容器")
    private final FunctionHolder functionHolder;

    @Description("脚本引擎初始化器")
    private ScriptEngineInitializer engineInitializer;

    public EngineConfig(Configuration configuration) {
        this.configuration = configuration;
        this.functionHolder = new FunctionHolder();
        this.packageNameList = new HashSet<>(NumberConstant.COMMON_SIZE);
    }

    public EngineConfig scan(Class<?>... rootClass) {
        Stream.of(rootClass)
                .map(ClassUtil::getPackage)
                .forEach(this::scan);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public EngineConfig scan(String... packageName) {
        if (packageName.length == 1) {
            this.packageNameList.add(packageName[0]);
        } else {
            this.packageNameList.addAll(Arrays.asList(packageName));
        }
        return this;
    }
}
