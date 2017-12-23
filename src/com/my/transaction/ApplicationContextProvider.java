package com.my.transaction;

/**
 * Created by tufei on 2017/12/23.
 */
public class ApplicationContextProvider {
    private static AbstractApplicationContext applicationContext;

    public static void setApplicationContext(AbstractApplicationContext context){
        if(applicationContext == null){
            synchronized (ApplicationContextProvider.class){
                if(null == applicationContext){
                    applicationContext = context;
                }
            }
        }
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
