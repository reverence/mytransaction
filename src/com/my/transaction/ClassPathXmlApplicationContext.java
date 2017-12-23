package com.my.transaction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tufei on 2017/12/10.
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();//如果有多个，需要处理顺序问题

    public ClassPathXmlApplicationContext(String configXmlLocation) throws Exception{
        /**
         * 1,parse xml
         */
        beanDefinitionMap = XmlParser.parse(configXmlLocation);
        /**
         * 2,init instance
         */
        for(String key : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(key);
            Object bean = createBean(beanDefinition);
            if(bean instanceof BeanPostProcessor){
                beanPostProcessors.add((BeanPostProcessor) bean);
            }
            beanMap.put(key, bean);
        }
        /**
         * beanPostProcessor
         */
        for(String key : beanMap.keySet()){
            Object object = beanMap.get(key);
            for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
                object = beanPostProcessor.processBeanAfterInstance(object);
            }
            beanMap.put(key,object);
        }
    }

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        return beanMap.get(beanName);
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
        Class cls = Class.forName(className);
        //constructor args?
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgList();
        List<Property> properties = beanDefinition.getProperties();
        if(null != constructorArgs && constructorArgs.size()>0){
            List<Class> argTypes = new ArrayList<Class>();
            Constructor[] constructors = cls.getDeclaredConstructors();
            for(ConstructorArg arg : constructorArgs){
                Field field = cls.getDeclaredField(arg.getName());
                argTypes.add(field.getType());
            }
            Constructor ct = null;
            for(int i=0;i<constructors.length;i++){
                Constructor constructor = constructors[i];
                Class[] classes = constructor.getParameterTypes();
                if(classes.length == argTypes.size()){
                    int j = 0;
                    for(j=0;j<classes.length;j++){
                        if(!argTypes.get(j).getName().equals(classes[j].getName())){
                            break;
                        }
                    }
                    if(j == classes.length){
                        ct = constructor;
                        break;
                    }
                }
            }
            Object[] obj = new Object[argTypes.size()];
            int i = 0;
            for(ConstructorArg arg : constructorArgs){
                obj[i++] = arg.getValue();
            }
            Object object = ct.newInstance(obj);
            return object;
        }else if(properties != null && properties.size() > 0){
            Object object = cls.newInstance();
            for(Property property : properties){
                String name = property.getName();
                String value = property.getValue();
                String ref = property.getRef();
                //get method
                Method method = getMethod(cls,name);
                if(value != null){
                    method.invoke(object,value);
                }else if(null != ref && !"".equals(ref.trim())){
                    Object bean = beanMap.get(ref);
                    if(null == bean) {
                        bean = createBean(beanDefinitionMap.get(ref));
                    }
                    method.invoke(object,bean);
                    return  object;
                }
            }
        }

        return cls.newInstance();
    }

    private Method getMethod(Class cls,String name){
        String methodName = "set" + name.substring(0,1).toUpperCase()+name.substring(1);
        for(Method method : cls.getDeclaredMethods()){
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }
}
