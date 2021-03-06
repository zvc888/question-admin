package com.question.admin.domain.entity.sysmgr;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("st_user")
public class User extends BaseEntity<User> {

    private static final long serialVersionUID = 1L;

    private String account;

    private String name;

    private String password;
    @TableField("session_key")
    private String sessionKey;
    @TableField("wx_open_id")
    private String wxOpenId;
    private String email;

    @TableField("last_pwd_modified_time")
    private Date lastPwdModifiedTime;

    private String status;
    private String phone;

    @TableField("erp_flag")
    private String erpFlag;

    /**
     * 有效标志
     */
    @TableField("yn_flag")
    private String ynFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private Date modifiedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
