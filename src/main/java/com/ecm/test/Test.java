package com.ecm.test;


import com.ecm.mapper.ExcelMapper;
import com.ecm.model.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * @author rongyaowen
 * @create 2019-02-27 22:52
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        ExcelMapper<User> userExcelMapper = new ExcelMapper<>(new XSSFWorkbook("F:/Test.xlsx"), "F:/Test.xlsx");
        ExcelMapper<User> userExcelMapper2 = new ExcelMapper<>("F:/Test.xlsx");
        List<User> users = userExcelMapper.readAll(User.class);
        for (User user : users) {
            System.out.println(user);
            System.out.println(userExcelMapper2.insert(user));
        }
    }
}
