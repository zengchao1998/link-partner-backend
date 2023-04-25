package com.wut.self.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * @author zeng
 */
@Data
public class TeamJoinRequest implements Serializable {


    private static final long serialVersionUID = 1485588807067155779L;

    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队伍申请密码
     */
    private String teamPassword;
}
