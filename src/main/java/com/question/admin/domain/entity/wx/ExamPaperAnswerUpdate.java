package com.question.admin.domain.entity.wx;

import lombok.Data;

@Data
public class ExamPaperAnswerUpdate {
    private Integer id;
    private Integer customerScore;
    private Boolean doRight;
}
