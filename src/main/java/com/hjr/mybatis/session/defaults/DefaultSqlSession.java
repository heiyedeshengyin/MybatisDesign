package com.hjr.mybatis.session.defaults;

import com.hjr.mybatis.cfg.Configuration;
import com.hjr.mybatis.session.SqlSession;
import com.hjr.mybatis.session.proxy.MapperProxy;
import com.hjr.mybatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * SqlSession接口的默认实现类
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration cfg;
    private Connection connection;

    /**
     * 从DefaultSqlSessionFactory类获取mybatis配置对象<br>
     * 再根据配置从DataSourceUtil类获取数据库连接
     * @param cfg mybatis配置对象
     */
    public DefaultSqlSession(Configuration cfg) {
        this.cfg = cfg;
        this.connection = DataSourceUtil.getConnection(cfg);
    }

    /**
     * 通过动态代理获取dao接口的实现对象
     * @param daoInterfaceClass dao接口字节码
     * @param <T> dao接口类型
     * @return dao接口实现对象
     */
    @Override
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T) Proxy.newProxyInstance(
                daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(cfg.getMappers(), connection)
        );
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
