package com.my.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tufei on 2017/12/16.
 */
@MyService
public class TransactionBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object processBeanAfterInstance(Object bean) {
        Set<Method> methodList = new HashSet<Method>();
        Class cls = bean.getClass();
        Class[] interfaces = cls.getInterfaces();
        if(null != interfaces){//jdk动态代理一定是实现了接口的
            for(Class inter: interfaces){
                Method[] methods = inter.getDeclaredMethods();
                for(Method method : methods){
                    try {
                        Method m = cls.getDeclaredMethod(method.getName());
                        if(m.isAnnotationPresent(Transactional.class)){
                            methodList.add(m);
                        }
                        //others
                    } catch (NoSuchMethodException e) {
                        //ignore
                    }
                }
            }
        }
        if(!methodList.isEmpty()){
            return createProxy(bean,methodList);
        }
        return bean;
    }

    private Object createProxy(Object bean, final Set<Method> methodList) {
        class AopMethodHandler implements InvocationHandler {
            private Object o;
            public AopMethodHandler(Object obj){
                this.o = obj;
            }
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
                Method m = getMethod(methodList,method,Transactional.class);
                if (null != m) {
                    Transactional transactional = m.getAnnotation(Transactional.class);
                    int currentIsolation = transactional.isolation();
                    String currentProgation = transactional.propagation();

                    TransactionManager.TransactionContext currentTransactionContext = getTransactionContext(currentIsolation,currentProgation);
                    Connection currentConnection = currentTransactionContext.getCurrentConnection();
                    if(null != ConnectionHolder.getConnectedCount(currentConnection)){
                        ConnectionHolder.setConnection(currentConnection,ConnectionHolder.getConnectedCount(currentConnection));
                    }else{
                        ConnectionHolder.setConnection(currentConnection);
                    }

                    try{
                        ConnectionHolder.incRequest();
                        Object object = method.invoke(o,args);
                        if(1 == ConnectionHolder.getConnectedCount(currentConnection))
                            currentConnection.commit();
                        return object;
                    }catch (Exception e){
                        currentConnection.rollback();
                        throw e;
                    }finally {
                        cleanUpTransactionContext(currentTransactionContext);
                    }
                }

                return method.invoke(o,args);
            }

            private void cleanUpTransactionContext(TransactionManager.TransactionContext currentTransactionContext)throws Exception {
                ConnectionHolder.releaseRequest();
                Connection currentConnection = currentTransactionContext.getCurrentConnection();
                if(TransactionManager.PROAGTION_REQUIRED_NEW.equalsIgnoreCase(currentTransactionContext.getPropagation())){
                    currentConnection.close();
                    ConnectionHolder.clear();
                }
                if(null != ConnectionHolder.getConnectedCount(currentConnection) && 0 == ConnectionHolder.getConnectedCount(currentConnection)){
                    if(!currentConnection.isClosed())
                        currentConnection.close();
                    ConnectionHolder.clear();
                }
                if(null != currentTransactionContext.getPrevTransactionContext()){
                    currentTransactionContext = currentTransactionContext.getPrevTransactionContext();
                    currentConnection = currentTransactionContext.getCurrentConnection();
                    currentConnection.setAutoCommit(currentTransactionContext.isAutoCommit());
                    int count = ConnectionHolder.getConnectedCount(currentConnection);
                    ConnectionHolder.setConnection(currentConnection,count);
                    TransactionManager.setTransactionContext(currentTransactionContext);
                }else{
                    //must be clear
                    Connection conn = ConnectionHolder.getCurrentConnection();
                    if(null != conn && !conn.isClosed()){
                        conn.close();
                    }
                    ConnectionHolder.clear();
                    TransactionManager.clear();
                }
            }

            private TransactionManager.TransactionContext getTransactionContext(int currentIsolation, String currentProgation) throws Exception {
                TransactionManager.TransactionContext transactionContext = TransactionManager.getTransactionContext();
                if(null == transactionContext){//当前不存在事务上下文
                    Connection connection = ConnectionProvider.getConnection();
                    connection.setTransactionIsolation(currentIsolation);
                    connection.setAutoCommit(false);
                    transactionContext = new TransactionManager.TransactionContext(connection,connection.getTransactionIsolation(),
                            currentProgation,false);
                    TransactionManager.setTransactionContext(transactionContext);
                }else{
                    if(TransactionManager.PROAGTION_REQUIRED_NEW.equalsIgnoreCase(currentProgation)){
                        //开辟一个新的连接
                        Connection newConnnection = ConnectionProvider.getConnection();
                        newConnnection.setTransactionIsolation(currentIsolation);
                        newConnnection.setAutoCommit(false);
                        TransactionManager.TransactionContext newTransactionContext = new TransactionManager.TransactionContext(newConnnection,
                                newConnnection.getTransactionIsolation(),currentProgation,false);
                        newTransactionContext.setPrevTransactionContext(transactionContext);
                        TransactionManager.setTransactionContext(newTransactionContext);
                        transactionContext = newTransactionContext;
                    }else{
                        TransactionManager.TransactionContext context = new TransactionManager.TransactionContext(transactionContext.getCurrentConnection(),
                                currentIsolation,currentProgation,false);
                        context.setPrevTransactionContext(transactionContext);
                        context.getCurrentConnection().setAutoCommit(false);
                        transactionContext = context;
                    }
                }
                return transactionContext;
            }

            private Method getMethod(Set<Method> methodList, Method method, Class cls) {
                for(Method m :methodList){
                    if(m.getName().equals(method.getName())){
                        if(m.isAnnotationPresent(cls)){
                            return m;
                        }
                    }
                }
                return null;
            }
        }
        return Proxy.newProxyInstance(bean.getClass().getClassLoader(),bean.getClass().getInterfaces(),new AopMethodHandler(bean));
    }
}
