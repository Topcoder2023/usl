package com.gitee.usl.grammar.asm;

/**
 * @author hongda.li
 */
public class GlobalClassLoader extends ClassLoader {

    public GlobalClassLoader(ClassLoader parent) {
        super(parent);
    }


    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

}
