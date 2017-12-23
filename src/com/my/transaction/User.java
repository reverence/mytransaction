package com.my.transaction;

/**
 * Created by tufei on 2017/12/10.
 */
public class User {

    private Integer id;

    private String userName;

    private int age;

    private String sex;

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public Integer getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
