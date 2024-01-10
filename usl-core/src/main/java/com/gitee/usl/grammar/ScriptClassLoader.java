package com.gitee.usl.grammar;

import com.gitee.usl.api.annotation.Description;

/**
 * @author hongda.li
 */
@Description("脚本类加载器")
public class ScriptClassLoader extends ClassLoader {

    public ScriptClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

}
