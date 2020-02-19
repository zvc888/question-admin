package com.question.admin.service.exam;


import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.vo.student.paper.ExamPaperQuestionCustomerAnswer;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitItemVM;

public interface ExamPaperQuestionCustomerAnswerService extends IService<ExamPaperQuestionCustomerAnswer> {
    /**
     * 试卷问题答题信息转成ViewModel 传给前台
     *
     * @param qa ExamPaperQuestionCustomerAnswer
     * @return ExamPaperSubmitItemVM
     */
    ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa);
}
