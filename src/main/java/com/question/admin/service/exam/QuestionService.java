package com.question.admin.service.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.exam.Question;
import com.question.admin.domain.vo.manager.exam.question.QuestionEditRequestVM;

public interface QuestionService extends IService<Question> {
    Question insertFullQuestion(QuestionEditRequestVM model);

    Question updateFullQuestion(QuestionEditRequestVM model);

    QuestionEditRequestVM getQuestionEditRequestVM(Long questionId);

    QuestionEditRequestVM getQuestionEditRequestVM(Question question);
}
