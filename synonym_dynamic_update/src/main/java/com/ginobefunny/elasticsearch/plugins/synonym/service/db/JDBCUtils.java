package com.ginobefunny.elasticsearch.plugins.synonym.service.db;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: seastliu
 * @Description:
 * @Date: Create in 11:13 2019/11/1
 * @Modified by:
 **/
public class JDBCUtils {

    private static final Logger LOGGER = ESLoggerFactory.getLogger(Monitor.class.getName());

    public static List<String> queryWordList(QueryDbDto queryDbDto) {
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(queryDbDto.getUrl(),
                    queryDbDto.getUser(), queryDbDto.getPassword());
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryDbDto.getSql());
            while (rs.next()) {
                String rule = rs.getString("word");
                list.add(rule);
            }
        } catch (Exception e) {
            LOGGER.error("query word list failed, sql is " + queryDbDto.getSql() + ", error is ", e);
        } finally {
            closeQuietly(conn, stmt, rs);
        }

        return list;
    }

    private static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("resultSet close failed, error is ", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.error("statement close failed, error is ", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("connection close failed, error is ", e);
            }
        }
    }

    public static void main(String[] args) {
        QueryDbDto queryDbDto = new QueryDbDto();
        queryDbDto.setUrl("jdbc:mysql://168.63.117.157:3306/zlcft?useUnicode=true&serverTimezone=GMT%2B8&useSSL=false&&characterEncoding=UTF-8");
        queryDbDto.setUser("zlcft");
        queryDbDto.setPassword("zlcft");
        queryDbDto.setSql("SELECT word FROM dynamic_synonym_rule");
        System.out.println(queryWordList(queryDbDto));
    }
}
