package com.question.admin.config.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zvc
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;          // 主键ID
    private String account;      // 账号
    private String name;         // 姓名
    private Long orgId;      // 组织ID


    public LoginUser() {
    }

    public LoginUser(String account, Long userId, String name) {
        this.account = account;
        this.userId = userId;
        this.name = name;
    }

}
