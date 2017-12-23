package com.my.transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tufei on 2017/12/13.
 */
public abstract  class AbstractApplicationContext implements BeanFactory {

    protected Map<String,Object> beanMap = new HashMap<String,Object>();//存放创建的bean

    protected Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 处理容器关闭
     */
    public void close(){
        for(String key : beanDefinitionMap.keySet()){
            BeanDefinition definition = beanDefinitionMap.get(key);
            String destroyMethod = definition.getDestroyMethod();
            if(null != destroyMethod && !"".equals(destroyMethod.trim())){
                //反射调用对象的destroy方法
            }
        }

        beanMap.clear();
        beanDefinitionMap.clear();
        beanMap = null;
        beanDefinitionMap = null;
    }
}
