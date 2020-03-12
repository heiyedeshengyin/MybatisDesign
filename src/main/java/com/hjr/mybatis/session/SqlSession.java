package com.hjr.mybatis.session;

/**
 * 通过动态代理返回实现dao接口的对象<br>
 * 代理模式<br>
 * 不改变源码的情况对已有方法进行增强
 */
public interface SqlSession {
    /**
     * 返回实现dao接口的对象
     * @param daoInterfaceClass dao接口字节码
     * @param <T> dao接口类型
     * @return dao接口实现对象
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    void close();
}
