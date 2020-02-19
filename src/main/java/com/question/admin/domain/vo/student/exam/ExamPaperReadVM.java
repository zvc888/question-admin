package com.question.admin.domain.vo.student.exam;

import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;
import lombok.Data;

@Data
public class ExamPaperReadVM {
    private ExamPaperEditRequestVM paper;
    private ExamPaperSubmitVM answer;
}
