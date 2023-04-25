package com.wut.self.model.enums;

import lombok.Getter;

/**
 * @author zeng
 */
@Getter
public enum TeamStatusEnum {

    PUBLIC_STATUS(0, "公开状态"),
    PRIVATE_STATUS(1, "私有状态"),
    SECRET_STATUS(2, "加密状态");

    private final int statusCode;

    private final String statusDescription;

    TeamStatusEnum(int statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    // 根据状态码获取枚举对象
    public static TeamStatusEnum getEnumByCode(Integer code) {
        if(code == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum value : values) {
            if(value.getStatusCode() == code) {
                return value;
            }
        }
        return null;
    }
}
