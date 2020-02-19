package com.question.admin.domain.vo.manager.exam.question;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QuestionEditRequestVM {

    private Integer id;
    @NotNull
    private Integer questionType;
    @NotNull
    private Long subjectId;
    @NotBlank
    private String title;

    private Integer categoryId;

    @Valid
    private List<QuestionEditItemVM> items;
    @NotBlank
    private String analyze;

    private List<String> correctArray;

    private String correct;
    @NotBlank
    private String score;

    @Range(min = 1, max = 5, message = "请选择题目难度")
    private Integer difficult;

    private Integer itemOrder;
}
