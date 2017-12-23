package com.my.transaction;

import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tufei on 2017/12/14.
 */
public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public void parse(Element element, Map<String, BeanDefinition> beanDefinitionMap) {
        String basePackage = element.attributeValue("base-package");
        if(null != basePackage && !"".equals(basePackage.trim())){
            List<String>classes = readClassesFromPackage(basePackage);
            for(String s : classes){
                Class cls = null;
                try {
                    cls = Class.forName(s);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if(cls.isAnnotationPresent(MyService.class)){
                    MyService value = (MyService) cls.getAnnotation(MyService.class);
                    String name = value.value();
                    if(null == name || "".equalsIgnoreCase(name.toString())){
                        String[] str = s.split("\\.");
                        name = str[str.length-1];
                        name = name.substring(0,1).toLowerCase()+name.substring(1);
                        BeanDefinition bean = new BeanDefinition();
                        bean.setClassName(s);
                        bean.setId(name);
                        if(beanDefinitionMap.containsKey(name))
                            throw new RuntimeException("repeat definition name:"+name);
                        beanDefinitionMap.put(name,bean);
                    }
                }
            }
        }
    }

    private List<String> readClassesFromPackage(String basePackage) {
        ArrayList<String>classList = new ArrayList<String>();
        basePackage = basePackage.replace(".","\\");
        String path = System.getProperty("java.class.path");
        String[] paths = path.split(";");
        String currentDir = System.getProperty("user.dir");
        for(String s : paths){
            if(!s.endsWith(".jar") && s.contains(currentDir)){//只有一个目录
                path = s;
                break;
            }
        }
        String dirPath = path+"\\"+basePackage;
        List<String> classes = new ArrayList<String>();
        getFiles(dirPath,classes);
        for(String s : classes){
            s = s.replace(path+"\\","");
            s = s.replace("\\",".");
            s = s.replace(".class","");
            classList.add(s);
        }
        return classList;
    }

    private void getFiles(String path,List<String>classes){
        File file = new File(path);
        if(file.exists()){
            File[] fileList = file.listFiles();
            if(null != fileList) {
                for(File f : fileList){
                    if(f.isDirectory()){
                        getFiles(f.getAbsolutePath(),classes);
                    }else{
                        String fileName = f.getName();
                        if(fileName.endsWith(".class")){
                            classes.add(f.toString());
                        }
                    }
                }
            }
        }
    }
}