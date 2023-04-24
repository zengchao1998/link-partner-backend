package com.wut.self.scripts;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zeng
 */
public class ImportTableUserData {

    public static void main(String[] args) {
        String fileName = "D:\\desktop\\Java\\project_summary\\user-center-backend-self\\src\\main\\resources\\dev.xlsx";
        List<TableUserInfo> userInfoList = EasyExcel.read(fileName).head(TableUserInfo.class).sheet().doReadSync();
        System.out.println("total counts = " + userInfoList.size());

        // 对数据进行去重
        Map<String, List<TableUserInfo>> listMap = userInfoList
                .stream()
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                .collect(Collectors.groupingBy(TableUserInfo::getUsername));
        System.out.println("distinct counts = " + listMap.keySet().size());
    }
}
