package com.question.admin.domain.entity.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.utils.ExamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bt_exam_question")
public class Question extends BaseEntity<Question> implements Serializable {

    //    private String name;
    private Integer status;
    @TableField("subject_id")
    private Long subjectId;
    @TableField("question_type")
    private Integer questionType;
    private Integer difficult;
    private String correct;
    @TableField("info_text_content_id")
    private Long infoTextContentId;
    private String creator;
    @TableField("creator_id")
    private Long creatorId;

    private String editor;
    @TableField("created_time")
    private Date createdTime;
    private Integer score;
    /**
     * 修改时间
     */
    @TableField("modified_time")
    private Date modifiedTime;

    public void setCorrectFromVM(String correct, List<String> correctArray) {
        int qType = this.getQuestionType();
        if (qType == QuestionTypeEnum.MultipleChoice.getCode()) {
            String correctJoin = ExamUtil.contentToString(correctArray);
            this.setCorrect(correctJoin);
        } else {
            this.setCorrect(correct);
        }
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
