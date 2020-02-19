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
@TableName("bt_exam_paper")
public class Paper extends BaseEntity<Paper> implements Serializable {

    private String name;
    private String status;
    @TableField("subject_id")
    private Long subjectId;
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

    @TableField("suggest_time")
    private Integer suggestTime;
    @TableField("paper_type")
    private Integer paperType;
    @TableField("question_count")
    private Integer questionCount;
    @TableField("limit_end_time")
    private Date limitStartTime;
    @TableField("limit_end_time")
    private Date limitEndTime;
    @TableField("text_content_id")
    private Long textContentId;
    @TableField("task_exam_id")
    private Long taskExamId;
    private Integer score;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
