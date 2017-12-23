package com.my.transaction;

import org.dom4j.Element;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tufei on 2017/12/12.
 */
public interface BeanDefinitionParser {

    /**
     * 解析element
     * @param element
     * @param beanDefinitionMap
     */
    public void parse(Element element, Map<String, BeanDefinition> beanDefinitionMap) throws IOException;
}
