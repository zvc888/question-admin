package com.question.admin.domain.vo.manager.sysmgr;


import com.baomidou.mybatisplus.annotation.TableField;
import com.question.admin.domain.entity.sysmgr.Notice;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class NoticeReadVO extends Notice {

    private static final long serialVersionUID = -3842182350180882396L;

    @TableField("user_id")
    private Long userId;
    @TableField("read_time")
    private Date readTime;
    private Boolean isRead;

    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    private Date endTime;
}
