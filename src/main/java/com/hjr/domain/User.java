package com.hjr.domain;

import java.io.Serializable;

/**
 * Java Bean类<br>
 * 对应数据库中的user表
 */
public class User implements Serializable {
    //对应user表中的id字段
    private int id;
    //对应name表中的name字段
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
