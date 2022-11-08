package org.esni.siskin_core.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SiskinDBConnectionPoolUtil implements Serializable {

    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    /**
     * 释放资源
     * @param conn
     * @param st
     * @param rs
     */
    public static void closeAll(Connection conn, Statement st , ResultSet rs){
        closeResultSet(rs);
        closeStatement(st);
        closeConn(conn);
    }

    /**
     * 获取连接
     * @return 连接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     *释放连接
     * @param conn
     *     连接
     */
    public static void closeConn(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn = null ;
            }
        }
    }

    /**
     * 释放语句执行者
     * @param st
     * 语句执行者
     */
    public static void closeStatement(Statement st){
        if(st!=null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                st = null ;
            }
        }
    }

    /**
     * 释放结果集
     * @param rs
     * 结果集
     */
    public static void closeResultSet(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs = null ;
            }
        }
    }

    // 回收连接池
    public static void close() {

        if (ds != null) {
            ds.close();
        }

    }

}