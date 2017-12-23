package com.my.transaction;

/**
 * Created by tufei on 2017/12/16.
 */
public interface BeanPostProcessor {

    public Object processBeanAfterInstance(Object bean);
}
