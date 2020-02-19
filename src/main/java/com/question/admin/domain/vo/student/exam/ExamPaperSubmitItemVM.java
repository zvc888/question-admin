package com.question.admin.domain.vo.student.exam;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExamPaperSubmitItemVM {
    private Long id;
    @NotNull
    private Long questionId;

    private Boolean doRight;

    private String content;

    private Integer itemOrder;

    private List<String> contentArray;

    private String score;

    private String questionScore;
}
