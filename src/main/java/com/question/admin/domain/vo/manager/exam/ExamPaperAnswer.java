package com.question.admin.domain.vo.manager.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("bt_exam_paper_answer")
public class ExamPaperAnswer implements Serializable {

    private static final long serialVersionUID = -2143539181805283910L;

    private Long id;
    @TableField("paper_id")
    private Long paperId;
    @TableField("paper_name")
    private String paperName;
    @TableField("paper_type")
    private Integer paperType;
    @TableField("subject_id")
    private Long subjectId;
    @TableField("system_score")
    private Integer systemScore;
    @TableField("user_score")
    private Integer userScore;
    @TableField("paper_score")
    private Integer paperScore;
    @TableField("question_correct")
    private Integer questionCorrect;
    @TableField("question_count")
    private Integer questionCount;
    @TableField("do_time")
    private Integer doTime;

    private Integer status;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("created_time")
    private Date createdTime;
    @TableField("task_exam_id")
    private Long taskExamId;

}
