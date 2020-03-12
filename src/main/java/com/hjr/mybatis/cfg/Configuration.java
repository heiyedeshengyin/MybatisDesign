package com.hjr.mybatis.cfg;

import java.util.HashMap;
import java.util.Map;

/**
 * mybatis配置类<br>
 * 里面保存了数据库配置和所有mapper配置
 */
public class Configuration {
    //数据库驱动
    private String driver;
    //数据库url
    private String url;
    //数据库用户名
    private String username;
    //数据库密码
    private String password;

    //所有mapper配置
    //键为对应的方法具体位置(全类名.方法名)
    //值为对应的mapper对象,里面封装了SQL语句和返回值类型
    private Map<String, Mapper> mappers;

    public Configuration() {
        mappers = new HashMap<>();
    }

    public Map<String, Mapper> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, Mapper> mappers) {
        this.mappers.putAll(mappers);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
