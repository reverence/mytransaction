package com.my.transaction;

/**
 * Created by tufei on 2017/12/10.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContextProvider.setApplicationContext(context);
        IUserService userService = (IUserService) context.getBean("userService");
        userService.insertAndUpdate();
    }
}
