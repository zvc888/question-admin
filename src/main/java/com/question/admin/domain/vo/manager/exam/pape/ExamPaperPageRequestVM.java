package com.question.admin.domain.vo.manager.exam.pape;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamPaperPageRequestVM implements Serializable{

    private Integer id;
    private Integer subjectId;
    private Integer level;
    private Integer paperType;
    private Integer taskExamId;
    private Long createUser;
}
