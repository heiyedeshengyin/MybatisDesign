package com.hjr.mybatis.session.defaults;

import com.hjr.mybatis.cfg.Configuration;
import com.hjr.mybatis.session.SqlSession;
import com.hjr.mybatis.session.SqlSessionFactory;

/**
 * SqlSessionFactory接口的默认实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration cfg;

    /**
     * 从SqlSessionFactoryBuilder类获取mybatis配置对象
     * @param cfg
     */
    public DefaultSqlSessionFactory(Configuration cfg) {
        this.cfg = cfg;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}
