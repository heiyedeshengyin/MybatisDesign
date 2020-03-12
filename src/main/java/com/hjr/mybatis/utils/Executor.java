package com.hjr.mybatis.utils;

import com.hjr.mybatis.cfg.Mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责执行SQL语句<br>
 * 并封装结果集到一个List集合中
 */
public class Executor {
    /**
     * 执行SQL语句,并返回List集合
     * @param mapper mapper对象
     * @param conn 数据库连接对象
     * @param <E> 结果类型
     * @return 结果集的集合
     */
    public <E> List<E> selectList(Mapper mapper, Connection conn) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            //从mapper对象上获取SQL语句,在本项目中为select * from user
            String queryString = mapper.getQueryString();
            //从mapper对象上获取结果类型,在本项目中为com.hjr.domain.User
            String resultType = mapper.getResultType();
            //获取结果类型的字节码
            Class domainClass = Class.forName(resultType);

            //执行SQL语句
            pstm = conn.prepareStatement(queryString);
            //获取结果集
            rs = pstm.executeQuery();

            List<E> list = new ArrayList<>();
            //获取结果集的元信息
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            //遍历结果集的每一行
            while (rs.next()) {
                //创建结果类型的对象
                E obj = (E)domainClass.getDeclaredConstructor().newInstance();

                //遍历结果集的每一列
                for (int i = 1; i <= columnCount; i++) {
                    //获取每列的字段名
                    String columnName = rsmd.getColumnName(i);
                    //获取这一列,游标所在这一行的值
                    Object columnValue = rs.getObject(columnName);

                    //借助Java内省机制,实现结果类型属性的封装,使用前提为结果类型是Java Bean类,且属性名与第一个参数相同
                    PropertyDescriptor pd = new PropertyDescriptor(columnName, domainClass);
                    //获取结果类型的相应属性的set方法
                    Method writeMethod = pd.getWriteMethod();
                    //执行相应属性的set方法
                    writeMethod.invoke(obj, columnValue);
                }

                //将结果类型的对象封装到集合中
                list.add(obj);
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(pstm, rs);
        }
    }

    private void release(PreparedStatement pstm, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pstm != null) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
