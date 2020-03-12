package com.hjr.mybatis.session.proxy;

import com.hjr.mybatis.cfg.Mapper;
import com.hjr.mybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

/**
 * 实现动态代理的类
 */
public class MapperProxy implements InvocationHandler {
    private Map<String, Mapper> mappers;
    private Connection conn;

    /**
     * 从DefaultSqlSession类获取配置
     * @param mappers
     * @param conn
     */
    public MapperProxy(Map<String, Mapper> mappers, Connection conn) {
        this.mappers = mappers;
        this.conn = conn;
    }

    /**
     * 扩展dao接口中的findAll方法
     * @param proxy 被动态代理的对象
     * @param method 扩展的方法
     * @param args 该方法的参数
     * @return 动态代理后的对象
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        String key = className + "." + methodName;

        Mapper mapper = mappers.get(key);
        if (mapper == null) {
            throw new IllegalArgumentException("传入的参数有误");
        }

        //扩展后的方法返回值为封装结果集的List集合
        return new Executor().selectList(mapper, conn);
    }
}
