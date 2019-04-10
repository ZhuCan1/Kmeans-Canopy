package com.hust.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取Excel的工具类
 **/
public class ExcelReader {
    /**
     * 根据传入的路劲和列的索引读取文件指定列的内容
     **/
    public static List<String> read(String filePath, int index) {
        //保存读取的内容
        List<String> content = null;
        InputStream is = null;
        Workbook workbook = null;
        try {
            //判断该文件是否是excel文件
            is = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                System.err.println("读取的不是Excel文件");
                System.exit(0);
            }
            content = new ArrayList<String>();
            //获得第一张表
            Sheet sheet = workbook.getSheetAt(0);

            //sheet.getLastRowNum()获取有效的行数,
            //sheet中一行数据都没有则返回-1，只有第一行有数据则返回0，最后有数据的行是第n行则返回 n-1
            for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
                //依次遍历每一行
                Row row = sheet.getRow(i);
                if (row == null) {
                    //如果当前行为空，则存入“”，方便后面根据索引取数据，跳出当前循环
                    content.add("");
                    continue;
                }
                //sheet.getLastCellNum()获取有效的列数,和sheet.getLastRowNum()有区别
                //row中一列数据都没有则返回-1，只有第一列有数据则返回1，最后有数据的列是第n列则返回 n；
                //判断index是否符合范围
                if (index + 1 > row.getLastCellNum()) {
                    System.out.println("读取的列不存在");
                    return null;
                }
                if (row.getCell(index) != null) {
                    content.add(row.getCell(index).getStringCellValue());
                } else {
                    //如果该列无内容
                    content.add("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;

    }
}
