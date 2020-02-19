package com.question.admin.domain.entity.exam.question;

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
@TableName("bt_exam_text_content")
public class TextContent extends BaseEntity<TextContent> implements Serializable {


    private String content;
    @TableField("created_time")
    private Date createdTime;
    @TableField("creator_id")
    private Long creatorId;
    public TextContent(){}
    public TextContent(String content, Date createdTime, Long creatorId) {
        this.content = content;
        this.createdTime = createdTime;
        this.creatorId = creatorId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
