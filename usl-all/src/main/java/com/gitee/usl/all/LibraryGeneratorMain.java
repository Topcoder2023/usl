package com.gitee.usl.all;

import com.gitee.usl.infra.utils.LibraryGenerator;

/**
 * @author hongda.li
 */
public class LibraryGeneratorMain {
    public static void main(String[] args) {
        // USL 全部内置函数生成示例文件
        LibraryGenerator.newBuilder().all().build().generate();
    }
}
