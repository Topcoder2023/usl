package com.gitee.usl.resource;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @author hongda.li
 */
public class GlobalConfigAccessor {
    private static final String CONFIG_SUFFIX = "/USL/Config/";
    private static final String CONFIG_PATH = FileUtil.getTmpDirPath() + CONFIG_SUFFIX;
    private static final File SYSTEM = new File(CONFIG_PATH + "/system.json");

    static {
        if (!FileUtil.exist(SYSTEM)) {

        }
    }


    static final class System {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public System setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public System setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
