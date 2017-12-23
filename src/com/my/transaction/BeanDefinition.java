package com.my.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tufei on 2017/12/10.
 */
public class BeanDefinition {
    private String id;
    private String className;
    private String initMethod;
    private String destroyMethod;
    private List<ConstructorArg> constructorArgList;
    private List<Property> properties;

    public List<ConstructorArg> getConstructorArgList() {
        if(null == constructorArgList){
            constructorArgList = new ArrayList<ConstructorArg>();
        }
        return constructorArgList;
    }

    public List<Property> getProperties() {
        if(null == properties){
            properties = new ArrayList<Property>();
        }
        return properties;
    }

    public String getClassName() {
        return className;
    }

    public String getId() {
        return id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setConstructorArgList(List<ConstructorArg> constructorArgList) {
        this.constructorArgList = constructorArgList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getDestroyMethod() {
        return destroyMethod;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
}

class ConstructorArg{
    private String name;
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Property{
    private String name;
    private String value;
    private String ref;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRef() {
        return ref;
    }
    public String getValue() {
        return value;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
