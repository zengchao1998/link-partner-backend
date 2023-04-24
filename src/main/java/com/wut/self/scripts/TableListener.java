package com.wut.self.scripts;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

/**
 * @author zeng
 * 读取监听器
 * 在读取 Excel 表格数据时触发定义的相关方法
 */
public class TableListener implements ReadListener<TableUserInfo> {
    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    表格中的一行数据
     * @param context 上下文信息
     */
    @Override
    public void invoke(TableUserInfo data, AnalysisContext context) {
        System.out.println(data);
    }

    /**
     * 所有数据解析完成了都会来调用的方法
     *
     * @param context 上下文信息
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("ending");
    }
}

