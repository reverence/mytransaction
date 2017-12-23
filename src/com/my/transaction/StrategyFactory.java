package com.my.transaction;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tufei on 2017/12/12.
 */
public class StrategyFactory {

    /**
     * 存放bean定义对应的解析parser
     */
    private static ConcurrentHashMap<String,BeanDefinitionParser> beanDefinitionParserMap = new ConcurrentHashMap<String,BeanDefinitionParser>();

    static {
        //初始化各个定义对应的parser
        beanDefinitionParserMap.putIfAbsent("bean",new CommonBeanDefinitionParser());
        beanDefinitionParserMap.putIfAbsent("component-scan",new ComponentScanBeanDefinitionParser());
    }

    /**
     * 获取元素名称对应的parser
     * @param definitionName
     * @return
     */
    public static BeanDefinitionParser getBeanDefinitionParser(String definitionName){
        return beanDefinitionParserMap.get(definitionName);
    }

}
