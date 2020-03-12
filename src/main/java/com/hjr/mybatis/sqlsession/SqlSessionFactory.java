package com.hjr.mybatis.sqlsession;

/**
 * 用于生产SqlSession对象<br>
 * 工厂模式<br>
 * 解耦
 */
public interface SqlSessionFactory {
    /**
     * 用于创建SqlSession对象
     * @return
     */
    SqlSession openSession();
}
