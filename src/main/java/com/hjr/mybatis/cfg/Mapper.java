package com.hjr.mybatis.cfg;

/**
 * mapper配置类<br>
 * 里面封装了SQL语句和返回值类型
 */
public class Mapper {
    //SQL语句
    private String queryString;
    //返回值类型(全类名)
    private String resultType;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
