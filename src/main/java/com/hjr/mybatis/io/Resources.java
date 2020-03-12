package com.hjr.mybatis.io;

import java.io.InputStream;

/**
 * 用于获取mybatis配置文件的工具类
 */
public class Resources {
    private Resources() {}

    public static InputStream getResourceAsStream(String filPath) {
        return Resources.class.getClassLoader().getResourceAsStream(filPath);
    }
}
