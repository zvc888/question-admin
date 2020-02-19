package com.question.admin.domain.vo.manager.exam.question;

import com.question.admin.domain.vo.manager.exam.BaseVM;
import lombok.Data;

import java.util.Date;

@Data
public class QuestionResponseVM extends BaseVM {

    private Integer id;

    private Integer questionType;

    private Integer textContentId;

    private Date createdTime;

    private Date modifiedTime;

    private Integer subjectId;

    private Long createUserId;
    private String createUser;

    private String score;

    private Integer status;

    private String correct;

    private Integer analyzeTextContentId;

    private Integer difficult;

    private String shortTitle;

}
