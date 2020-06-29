package org.wltea.analyzer.dic.db;

/**
 * @Author: seastliu
 * @Description:
 * @Date: Create in 9:13 2019/11/1
 * @Modified by:
 **/
public class QueryDbDto {

    private String url;

    private String user;

    private String password;

    private String sql;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
