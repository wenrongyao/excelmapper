package com.ecm.model;

import com.ecm.annotation.Head;

import java.util.Date;

/**
 * @author rongyaowen
 * @create 2019-02-27 22:48
 **/
public class User {
    @Head(value = "姓名", orderBy = 0)
    private String name;
    @Head(value = "年龄", orderBy = 1)
    private Integer age;
    @Head(value = "地址", orderBy = 2)
    private String address;
    @Head(value = "出生年月", orderBy = 3)
    private Date birthDate;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
