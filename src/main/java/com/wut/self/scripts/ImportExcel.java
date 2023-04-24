package com.wut.self.scripts;


import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * @author zeng
 * 作用：用于读取 Excel 表格中的用户数据
 */
public class ImportExcel {

    public static void main(String[] args) {
        String fileName = "D:\\desktop\\Java\\project_summary\\user-center-backend-self\\src\\main\\resources\\dev.xlsx";
        // readByListener(fileName);
        synchronizedRead(fileName);
    }

    /**
     * 自定义监听器读取数据
     * @param fileName excel 文件
     */
    public static void readByListener(String fileName) {
        // 自定义监听器实现
        EasyExcel.read(fileName, TableUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读取数据，不推荐使用，数据量大时，会将数据存放到内存中，造成内存溢出
     * @param fileName excel 文件
     */
    public static void synchronizedRead(String fileName) {
        List<TableUserInfo> data = EasyExcel.read(fileName).head(TableUserInfo.class).sheet().doReadSync();
        data.forEach(System.out::println);
    }
}

