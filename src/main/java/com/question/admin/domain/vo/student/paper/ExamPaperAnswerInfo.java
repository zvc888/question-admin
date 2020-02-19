package com.question.admin.domain.vo.student.paper;

import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.vo.manager.exam.ExamPaperAnswer;
import lombok.Data;

import java.util.List;

@Data
public class ExamPaperAnswerInfo {
    public Paper paper;
    public ExamPaperAnswer examPaperAnswer;
    public List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers;
}
