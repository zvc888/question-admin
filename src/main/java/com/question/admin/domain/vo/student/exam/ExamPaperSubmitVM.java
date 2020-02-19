package com.question.admin.domain.vo.student.exam;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ExamPaperSubmitVM {

    @NotNull
    private Long id;

    @NotNull
    private Integer doTime;

    private String score;

    @NotNull
    @Valid
    private List<ExamPaperSubmitItemVM> answerItems;
}
