package com.question.admin.domain.vo.manager.exam.pape;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ExamPaperEditRequestVM {
    private Long id;

    @NotNull
    private Long subjectId;
    @NotNull
    private Integer paperType;
    @NotBlank
    private String name;
    @NotNull
    private Integer suggestTime;

    private List<String> limitDateTime;

    @Size(min = 1,message = "请添加试卷标题")
    @Valid
    private List<ExamPaperTitleItemVM> titleItems;

    private String score;

}
