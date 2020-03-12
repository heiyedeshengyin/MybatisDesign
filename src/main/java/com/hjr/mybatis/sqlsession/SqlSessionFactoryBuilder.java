package com.hjr.mybatis.sqlsession;

import com.hjr.mybatis.cfg.Configuration;
import com.hjr.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import com.hjr.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * 用于创建SqlSessionFactory对象<br>
 * 构建者模式<br>
 * 把对象的创建细节隐藏,使用者直接调用方法就能拿到对象
 */
public class SqlSessionFactoryBuilder {
    /**
     * 根据传入的mybatis配置对象<br>
     * 创建相应的SqlSessionFactory对象
     * @param config mybatis配置对象
     * @return SqlSessionFactory对象
     */
    public SqlSessionFactory build(InputStream config) {
        //从XMLConfigBuilder类获取mybatis配置
        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);

        //返回SqlSessionFactory对象
        return new DefaultSqlSessionFactory(cfg);
    }
}
