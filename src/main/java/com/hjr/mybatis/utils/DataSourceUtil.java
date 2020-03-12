package com.hjr.mybatis.utils;

import com.hjr.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库源对象<br>
 * 用于获取数据库连接
 */
public class DataSourceUtil {
    private DataSourceUtil() {}

    /**
     * 根据传入的mybatis配置<br>
     * 获取数据库的连接
     * @param cfg mybatis配置对象
     * @return 数据库连接
     */
    public static Connection getConnection(Configuration cfg) {
        try {
            Class.forName(cfg.getDriver());
            return DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
