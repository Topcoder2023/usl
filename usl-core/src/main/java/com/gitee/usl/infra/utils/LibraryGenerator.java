package com.gitee.usl.infra.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.constant.NumberConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.hutool.core.annotation.AnnotationUtil.hasAnnotation;

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

        functionSet.forEach(clazz -> {
            for (Method method : ReflectUtil.getMethods(clazz, method -> hasAnnotation(method, Function.class))) {
                this.generateForAnnotation(method, writer);
            }
        });
    }

    private void generateForAnnotation(Method method, FileWriter writer) {
        Function function = AnnotationUtil.getAnnotation(method, Function.class);
        String[] names = function.value();
        for (String functionName : names) {
            this.generateForAnnotation(functionName, method.getParameters(), writer);
        }
    }

    private void generateForAnnotation(String functionName, Parameter[] parameters, FileWriter writer) {
        final StringBuilder builder = new StringBuilder("declare function ").append(functionName).append("(");

        NumberWrapper.IntWrapper wrapper = NumberWrapper.ofIntWrapper();
        wrapper.set(NumberConstant.INDEX_OF_LOWER_A);

        for (Parameter parameter : parameters) {
            char ch = (char) wrapper.getAndIncrement();
            Class<?> paramType = parameter.getType();
            builder.append(ch);
            if (String.class.isAssignableFrom(paramType)) {
                builder.append(": string, ");
            } else if (Number.class.isAssignableFrom(paramType)
                    || int.class.isAssignableFrom(paramType)
                    || byte.class.isAssignableFrom(paramType)
                    || short.class.isAssignableFrom(paramType)
                    || long.class.isAssignableFrom(paramType)
                    || double.class.isAssignableFrom(paramType)
                    || float.class.isAssignableFrom(paramType)) {
                builder.append(": number, ");
            } else if (Boolean.class.isAssignableFrom(paramType) || boolean.class.isAssignableFrom(paramType)) {
                builder.append(": boolean, ");
            } else {
                builder.append(": any, ");
            }
        }

        if (parameters.length != 0) {
            // 删去最后的逗号和空格
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }

        // 拼接返回值
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
                    .map(name -> ClassUtil.scanPackage(name, clz -> hasAnnotation(clz, FunctionGroup.class)))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet()));
        }
    }
}
