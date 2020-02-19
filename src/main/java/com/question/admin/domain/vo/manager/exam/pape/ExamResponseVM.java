package com.question.admin.domain.vo.manager.exam.pape;

import lombok.Data;

@Data
public class ExamResponseVM {
    private Integer id;

    private String name;

    private Integer questionCount;

    private Integer score;

    private String createdTime;

    private String modifiedTime;

    private Integer createUser;

    private Integer subjectId;

    private Integer paperType;

    private Integer frameTextContentId;
}
