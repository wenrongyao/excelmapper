package com.ecm.mapper;

import com.ecm.annotation.Head;
import com.ecm.annotation.Transparent;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rongyaowen
 * @create 2019-02-27 21:19
 * <p>
 * 通用excelMapper
 **/
public class ExcelMapper<T> {
    private final static String WRITE = "write";
    private final static String READ = "read";

    private String filePath;
    private Workbook writeWorkbook;
    private Workbook readWorkbook;


    public ExcelMapper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取所有记录
     *
     * @param tClass 待生成实例class对象
     * @return
     */
    public List<T> readAll(Class<T> tClass) {
        Workbook workbook = getWorkbook(filePath, READ);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            Row header = sheet.getRow(0);// 头
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                Row content = sheet.getRow(j);
                try {
                    T t = newInstance(tClass, header, content);
                    list.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 生成实例
     *
     * @param tClass  待生成实例class对象
     * @param header  标题
     * @param content 内容行
     * @return
     */
    private T newInstance(Class<T> tClass, Row header, Row content) throws Exception {
        T t = tClass.newInstance();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class typeClass = field.getType();
            Method method = tClass.getMethod(methodName, typeClass);
            Head head = field.getAnnotation(Head.class);
            Transparent transparent = field.getAnnotation(Transparent.class);
            if (transparent == null) {
                String value = head.value();
                for (int i = 0; i < header.getLastCellNum(); i++) {
                    String excelHeadName = header.getCell(i).getStringCellValue();
                    if (value.trim().equals(excelHeadName.trim())) {
                        Object excelValue = getValue(content.getCell(i));
                        method.invoke(t, typeClass.getConstructor(String.class).newInstance(excelValue));
                    }
                }
            }
        }
        return t;
    }

    /**
     * 根据单元格的类型返回真实值
     *
     * @param cell
     * @return
     */
    private Object getValue(Cell cell) {
        Object obj;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                obj = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    obj = sdf.format(cell.getDateCellValue());
                } else {
                    obj = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case BLANK:
                obj = "";
                break;
            default:
                obj = cell.toString();
                break;
        }
        return obj;
    }

    /**
     * 存储实体对象
     *
     * @param t 行号
     * @return
     */
    public int insert(T t) {
        Workbook workbook = getWorkbook(filePath, WRITE);
        boolean isNew = false;
        int sheetNum = workbook.getNumberOfSheets();
        Sheet sheet;
        Row header = null;
        Row content;
        if (sheetNum == 0) {//新建
            sheet = workbook.createSheet();
            header = sheet.createRow(0);
            content = sheet.createRow(1);
            isNew = true;
        } else { //第一个sheet追加
            sheet = workbook.getSheetAt(0);
            content = sheet.createRow(sheet.getLastRowNum() + 1);
        }
        Class tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method = null;
            try {
                method = tClass.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Head head = field.getAnnotation(Head.class);
            Transparent transparent = field.getAnnotation(Transparent.class);
            if (transparent == null) {
                if (isNew) {//新建
                    header.createCell(head.orderBy()).setCellValue(head.value()); //生成头
                }
                try {
                    Object value = method.invoke(t);
                    if (null != value)
                        content.createCell(head.orderBy()).setCellValue(value.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 批量写入
     *
     * @param list 插入数量
     * @return
     */
    public int insertList(List<T> list) {
        for (T t : list) {
            insert(t);
        }
        return list.size();
    }

    /**
     * 根据后缀类型建立workbook对象
     *
     * @param filePath  文件路径
     * @param operaType 读或写
     * @return
     */
    private synchronized Workbook getWorkbook(String filePath, String operaType) {
        String fileType = filePath.substring((filePath.indexOf(".") + 1));
        //根据excel文件类型创建Workbook对象
        Workbook workbook = null;
        if (operaType.equals(WRITE)) {
            if (this.writeWorkbook == null) {
                try {
                    if (fileType.equals("xls")) {
                        workbook = new HSSFWorkbook(new FileInputStream(filePath));
                    } else if (fileType.equals("xlsx")) {
                        workbook = new XSSFWorkbook(new FileInputStream(filePath));
                    }
                } catch (IOException e) {
                    if (fileType.equals("xls")) {
                        workbook = new HSSFWorkbook();
                    } else if (fileType.equals("xlsx")) {
                        workbook = new XSSFWorkbook();
                    }
                }
                this.writeWorkbook = workbook;
            } else {
                workbook = this.writeWorkbook;
            }
        } else if (operaType.equals(READ)) {
            if (this.readWorkbook == null) {
                try {
                    if (fileType.equals("xls")) {
                        workbook = new HSSFWorkbook(new FileInputStream(filePath));
                    } else if (fileType.equals("xlsx")) {
                        workbook = new XSSFWorkbook(new FileInputStream(filePath));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.readWorkbook = workbook;
            } else {
                workbook = this.readWorkbook;
            }
        }
        return workbook;
    }
}
