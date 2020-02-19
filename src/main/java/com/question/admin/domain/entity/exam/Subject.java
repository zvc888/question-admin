package com.question.admin.domain.entity.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bt_exam_subject")
public class Subject extends BaseEntity<Subject> implements Serializable {

    private String name;
    private String status;
    @TableField("category_id")
    private Long categoryId;
    private String creator;
    @TableField("creator_id")
    private Long creatorId;
    private String editor;
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
