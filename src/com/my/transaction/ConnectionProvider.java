package com.my.transaction;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by tufei on 2017/12/22.
 */
public class ConnectionProvider {

    public synchronized static Connection getConnection() throws Exception{
        Class.forName("org.postgresql.Driver").newInstance();
        String url ="jdbc:postgresql://localhost:5432/dev";
        //myDB为数据库名
        String user="postgres";
        String password="cz19890801";
        Connection conn= DriverManager.getConnection(url,user,password);
        return conn;
    }
}
