package com.my.transaction;

import org.dom4j.Element;

import java.util.List;
import java.util.Map;

/**
 * Created by tufei on 2017/12/12.
 */
public class CommonBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public void parse(Element element, Map<String,BeanDefinition> beanDefinitionMap) {
        BeanDefinition bean = new BeanDefinition();
        String id = element.attributeValue("id");
        String classname = element.attributeValue("class");
        bean.setId(id);
        bean.setClassName(classname);
        String initMethod = element.attributeValue("init-method");
        bean.setInitMethod(initMethod);
        String destroyMethod = element.attributeValue("destroy-method");
        bean.setDestroyMethod(destroyMethod);
        //todo 后续如果bean下面的子节点增加得很多，这里可以再进行改造
        /**
         * constructor-arg
         */
        List<Element> constructorArgs = element.elements("constructor-arg");
        if(null != constructorArgs && constructorArgs.size()>0){
            processConstructorArgs(bean,constructorArgs);
        }
        /**
         * property
         */
        List<Element> properties = element.elements("property");
        if(null != properties && properties.size()>0) {
            processProperties(bean,properties);
        }
        if(beanDefinitionMap.containsKey(id)){
            throw new RuntimeException("repeat bean definition on "+id);
        }
        beanDefinitionMap.put(id,bean);
    }

    private void processProperties(BeanDefinition bean, List<Element> properties) {
        for(Element e : properties){
            Property p = new Property();
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            String ref = e.attributeValue("ref");
            p.setName(name);
            p.setValue(value);
            p.setRef(ref);
            bean.getProperties().add(p);
        }
    }

    private void processConstructorArgs(BeanDefinition bean, List<Element> constructorArgs) {
        for(Element e : constructorArgs){
            ConstructorArg arg = new ConstructorArg();
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            arg.setName(name);
            arg.setValue(value);
            bean.getConstructorArgList().add(arg);
        }
    }
}
