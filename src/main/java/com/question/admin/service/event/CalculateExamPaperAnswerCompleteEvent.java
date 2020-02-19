package com.question.admin.service.event;

import com.question.admin.domain.vo.student.paper.ExamPaperAnswerInfo;
import org.springframework.context.ApplicationEvent;

public class CalculateExamPaperAnswerCompleteEvent extends ApplicationEvent {


    private final ExamPaperAnswerInfo examPaperAnswerInfo;

    public CalculateExamPaperAnswerCompleteEvent(final ExamPaperAnswerInfo examPaperAnswerInfo) {
        super(examPaperAnswerInfo);
        this.examPaperAnswerInfo = examPaperAnswerInfo;
    }

    public ExamPaperAnswerInfo getExamPaperAnswerInfo() {
        return examPaperAnswerInfo;
    }

}
