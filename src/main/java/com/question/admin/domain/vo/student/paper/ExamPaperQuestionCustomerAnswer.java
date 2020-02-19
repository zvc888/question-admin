package com.question.admin.domain.vo.student.paper;

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
@TableName("bt_exam_paper_question_customer_answer")
public class ExamPaperQuestionCustomerAnswer extends BaseEntity<ExamPaperQuestionCustomerAnswer> implements Serializable {

    private static final long serialVersionUID = 3389482731220342366L;

    @TableField("question_id")
    private Long questionId;
    @TableField("exam_paper_id")
    private Long examPaperId;
    @TableField("exam_paper_answer_id")
    private Long examPaperAnswerId;
    @TableField("question_type")
    private Integer questionType;
    @TableField("subject_id")
    private Long subjectId;
    @TableField("customer_score")
    private Integer customerScore;
    @TableField("question_score")
    private Integer questionScore;
    @TableField("question_text_content_id")
    private Long questionTextContentId;
    @TableField("answer")
    private String answer;
    @TableField("text_content_id")
    private Long textContentId;
    @TableField("do_right")
    private Boolean doRight;
    @TableField("creator_id")
    private Long creatorId;
    @TableField("created_time")
    private Date createTime;
    @TableField("item_order")
    private Integer itemOrder;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
