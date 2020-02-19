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
@TableName("bt_exam_category")
public class Category extends BaseEntity<Category> implements Serializable {
    private String name;
    private String status;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("creator")
    private String creator;
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
