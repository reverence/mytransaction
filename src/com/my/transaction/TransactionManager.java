package com.my.transaction;

import java.sql.Connection;

/**
 * Created by tufei on 2017/12/21.
 */
public class TransactionManager {

    public static final String PROGATION_REQUIRED = "REQUIRED";
    public static final String PROAGTION_REQUIRED_NEW = "REQUIRED_NEW";
    private static ThreadLocal<TransactionContext> transactionContextThreadLocal = new ThreadLocal<TransactionContext>();

    public static TransactionContext getTransactionContext() {
        return transactionContextThreadLocal.get();
    }

    public static void setTransactionContext(TransactionContext context){
        transactionContextThreadLocal.set(context);
    }

    public static void clear() {
        transactionContextThreadLocal.remove();
    }

    static class TransactionContext{//事务上下文信息
        private Connection currentConnection;//当前连接
        private int isolation;//当前隔离级别
        private String propagation;//当前传播属性
        private boolean autoCommit;
        private TransactionContext prevTransactionContext;

        public TransactionContext(){

        }

        public TransactionContext(Connection currentConnection,int isolation,String progation,boolean autoCommit){
            this.autoCommit = autoCommit;
            this.currentConnection = currentConnection;
            this.isolation = isolation;
            this.propagation = progation;
        }

        public boolean isAutoCommit() {
            return autoCommit;
        }

        public void setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
        }

        public void setIsolation(int isolation) {
            this.isolation = isolation;
        }
        public Connection getCurrentConnection() {
            return currentConnection;
        }

        public int getIsolation() {
            return isolation;
        }

        public String getPropagation() {
            return propagation;
        }

        public TransactionContext getPrevTransactionContext() {
            return prevTransactionContext;
        }

        public void setCurrentConnection(Connection currentConnection) {
            this.currentConnection = currentConnection;
        }

        public void setPrevTransactionContext(TransactionContext prevTransactionContext) {
            this.prevTransactionContext = prevTransactionContext;
        }

        public void setPropagation(String propagation) {
            this.propagation = propagation;
        }
    }
}
