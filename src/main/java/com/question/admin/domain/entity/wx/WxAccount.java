package com.question.admin.domain.entity.wx;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by EalenXie on 2018/11/26 10:26.
 * 实体 属性描述 这里只是简单示例，你可以自定义相关用户信息
 */

@Data
public class WxAccount {

    private Integer id;
    private String wxOpenid;
    private String sessionKey;
    private Date lastTime;

}