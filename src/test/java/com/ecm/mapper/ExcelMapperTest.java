package com.ecm.mapper;


import com.ecm.model.User;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by rongyaowen
 * on 2019/2/28.
 */
public class ExcelMapperTest {
    @Test
    public void readAll() throws Exception {
        ExcelMapper<User> userExcelMapper = new ExcelMapper<>("F:/Test.xlsx");
        List<User> users = userExcelMapper.readAll(User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void insert() throws Exception {
        ExcelMapper<User> userExcelMapper2 = new ExcelMapper<>("F:/Test.xlsx");
        User user = new User();
        user.setName("张三");
        user.setAge(25);
        user.setAddress("广东省广州市");
        user.setBirthDate(new Date());
        System.out.println(userExcelMapper2.insert(user));
    }


}
