package com.question.admin.domain.vo.student.paper;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamPaperAnswerPageResponseVM implements Serializable{
    private Integer id;

    private String createdTime;

    private String userScore;

    private String subjectName;

    private Integer subjectId;

    private Integer questionCount;

    private Integer questionCorrect;

    private String paperScore;

    private String doTime;

    private Integer paperType;

    private String systemScore;

    private Integer status;

    private String paperName;
}
