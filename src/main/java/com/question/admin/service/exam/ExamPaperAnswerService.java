package com.question.admin.service.exam;


import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.exam.ExamPaperAnswer;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitVM;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerInfo;

public interface ExamPaperAnswerService extends IService<ExamPaperAnswer> {

    /**
     * 计算试卷提交结果(不入库)
     *
     * @param examPaperSubmitVM
     * @param user
     * @return
     */
    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user);
    /**
     * 试卷答题信息转成ViewModel 传给前台
     *
     * @param id 试卷id
     * @return ExamPaperSubmitVM
     */
    ExamPaperSubmitVM examPaperAnswerToVM(Long id);
}
