package com.question.admin.domain.vo.student.paper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("bt_task_exam_customer_answer")
public class TaskExamCustomerAnswer extends BaseEntity<TaskExamCustomerAnswer> implements Serializable {

    private static final long serialVersionUID = -556842372977600137L;

    private Long id;

    @TableField("task_exam_id")
    private Long taskExamId;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("created_time")
    private Date createdTime;
    @TableField("text_content_id")
    private Integer textContentId;

}
