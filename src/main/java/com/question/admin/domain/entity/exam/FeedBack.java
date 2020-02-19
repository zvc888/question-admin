package com.question.admin.domain.entity.exam;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bt_feedback")
public class FeedBack extends BaseEntity<FeedBack> implements Serializable {
    private String phone;
    @NotBlank
    private String wechat;
    @NotNull
    private String content;
    private Integer status;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("editor_id")
    private Long editorId;
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
