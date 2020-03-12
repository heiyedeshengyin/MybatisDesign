package com.hjr.dao;

import com.hjr.domain.User;
import com.hjr.mybatis.anno.Select;

import java.util.List;

/**
 * 数据访问层<br>
 * Data Access Object<br>
 * 对应数据库中的user表
 */
public interface UserDao {
    @Select("select * from user")
    List<User> findAll();
}
