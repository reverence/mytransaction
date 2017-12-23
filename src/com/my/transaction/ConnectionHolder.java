package com.my.transaction;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tufei on 2017/12/23.
 */
public class ConnectionHolder {

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    private static Map<Connection,Integer> connectedCount = new ConcurrentHashMap<Connection, Integer>();

    public static void setConnection(Connection connection){
        connectionThreadLocal.set(connection);
        connectedCount.put(connection,0);
    }

    public static Connection getCurrentConnection(){
        return connectionThreadLocal.get();
    }

    public static Integer getConnectedCount(Connection connection){
        return connectedCount.get(connection);
    }

    public static void incRequest() {
        Connection connection = connectionThreadLocal.get();
        Integer count = connectedCount.get(connection);
        count = count+1;
        connectedCount.put(connection,count);
    }

    public static void releaseRequest(){
        Connection connection = connectionThreadLocal.get();
        if(null != connection){
            Integer count = connectedCount.get(connection);
            count = count-1;
            connectedCount.put(connection,count);
        }
    }

    public static void clear() {
        Connection connection = connectionThreadLocal.get();
        connectionThreadLocal.remove();
        if(null != connection)
            connectedCount.remove(connection);
    }

    public static void setConnection(Connection currentConnection, int count) {
        connectionThreadLocal.set(currentConnection);
        connectedCount.put(currentConnection,count);
    }
}
