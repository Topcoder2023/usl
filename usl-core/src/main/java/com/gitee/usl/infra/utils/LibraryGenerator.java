package com.gitee.usl.infra.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.constant.NumberConstant;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class LibraryGenerator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final File output;
    private final Set<Class<?>> functionSet;

    public LibraryGenerator(File output, Set<Class<?>> functionSet) {
        this.output = output;
        this.functionSet = functionSet;
    }

    public static LibraryGeneratorBuilder newBuilder() {
        return new LibraryGeneratorBuilder();
    }

    public void generate() {
        logger.debug("USL函数库示例文件输出目录 - [{}]", output.getAbsolutePath());
        final FileWriter writer = new FileWriter(output.getAbsolutePath() + "/function.d.ts");
        writer.write(CharSequenceUtil.EMPTY);

        for (Class<?> clazz : functionSet) {
            if (AviatorFunction.class.isAssignableFrom(clazz)) {
                this.generateForAviator(clazz, writer);
            }
            if (AnnotationUtil.hasAnnotation(clazz, FunctionGroup.class)) {
                for (Method method : ReflectUtil.getMethods(clazz, method -> AnnotationUtil.hasAnnotation(method, Function.class))) {
                    this.generateForAnnotation(method, writer);
                }
            }
        }
    }

    private void generateForAviator(Class<?> clazz, FileWriter writer) {

    }

    private void generateForAnnotation(Method method, FileWriter writer) {
        Function function = AnnotationUtil.getAnnotation(method, Function.class);
        String[] names = function.value();
        for (String functionName : names) {
            this.generateForAnnotation(functionName, method, writer);
        }
    }

    private void generateForAnnotation(String functionName, Method method, FileWriter writer) {
        StringBuilder builder = new StringBuilder("declare function ").append(functionName).append("(");
        int count = method.getParameterCount();
        int varIndex = 97;
        for (int i = 0; i < count; i++) {
            char ch = (char) varIndex;
            builder.append(ch).append(": any, ");
            varIndex = varIndex + 1;
        }
        if (count != 0) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("): any;\n");
        writer.append(builder.toString());
        writer.append("\n");
    }

    public static final class LibraryGeneratorBuilder {
        public static final String DEFAULT_OUTPUT = "usl@types";
        private File output;
        private final Set<String> functionSet = new HashSet<>(NumberConstant.COMMON_SIZE);

        public LibraryGeneratorBuilder all() {
            return this.packageName(USLRunner.class);
        }

        public LibraryGeneratorBuilder aviator() {
            return this.packageName(AbstractFunction.class);
        }

        public LibraryGeneratorBuilder output(String path) {
            this.output = new File(path);
            return this;
        }

        public LibraryGeneratorBuilder packageName(Class<?> clazz) {
            this.functionSet.add(ClassUtil.getPackage(clazz));
            return this;
        }

        public LibraryGeneratorBuilder packageName(String packageName) {
            this.functionSet.add(packageName);
            return this;
        }

        public LibraryGenerator build() {
            if (output == null) {
                output = new File(DEFAULT_OUTPUT);
            }
            return new LibraryGenerator(output, functionSet.stream()
                    .map(name -> ClassUtil.scanPackage(name, this::accept))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet()));
        }

        private boolean accept(Class<?> clz) {
            return AviatorFunction.class.isAssignableFrom(clz) || AnnotationUtil.hasAnnotation(clz, FunctionGroup.class);
        }
    }
}
