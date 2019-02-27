package com.ecm.model;

import com.ecm.annotation.Head;

/**
 * @author rongyaowen
 * @create 2019-02-27 22:48
 **/
public class User {
    @Head(value = "姓名", orderBy = 0)
    private String name;
    @Head(value = "年龄", orderBy = 1)
    private String age;
    @Head(value = "地址", orderBy = 2)
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
