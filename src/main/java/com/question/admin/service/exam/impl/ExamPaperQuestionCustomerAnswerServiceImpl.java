package com.question.admin.service.exam.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.vo.student.paper.ExamPaperQuestionCustomerAnswer;
import com.question.admin.mapper.exam.ExamPaperQuestionCustomerAnswerMapper;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitItemVM;
import com.question.admin.service.exam.ExamPaperQuestionCustomerAnswerService;
import com.question.admin.service.exam.TextContentService;
import com.question.admin.utils.ExamUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExamPaperQuestionCustomerAnswerServiceImpl extends ServiceImpl<ExamPaperQuestionCustomerAnswerMapper, ExamPaperQuestionCustomerAnswer> implements ExamPaperQuestionCustomerAnswerService {

    private final TextContentService textContentService;
    @Override
    public ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa) {
        ExamPaperSubmitItemVM examPaperSubmitItemVM = new ExamPaperSubmitItemVM();
        examPaperSubmitItemVM.setId(qa.getId());
        examPaperSubmitItemVM.setQuestionId(qa.getQuestionId());
        examPaperSubmitItemVM.setDoRight(qa.getDoRight());
        examPaperSubmitItemVM.setItemOrder(qa.getItemOrder());
        examPaperSubmitItemVM.setQuestionScore(ExamUtil.scoreToVM(qa.getQuestionScore()));
        examPaperSubmitItemVM.setScore(ExamUtil.scoreToVM(qa.getCustomerScore()));
        setSpecialToVM(examPaperSubmitItemVM, qa);
        return examPaperSubmitItemVM;
    }

    private void setSpecialToVM(ExamPaperSubmitItemVM examPaperSubmitItemVM, ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum) {
            case MultipleChoice:
                examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                examPaperSubmitItemVM.setContentArray(ExamUtil.contentToArray(examPaperQuestionCustomerAnswer.getAnswer()));
                break;
            case GapFilling:
                TextContent textContent = textContentService.getById(examPaperQuestionCustomerAnswer.getTextContentId());
                List<String> correctAnswer = JSON.parseArray(textContent.getContent(), String.class);
                examPaperSubmitItemVM.setContentArray(correctAnswer);
                break;
            default:
                if (QuestionTypeEnum.needSaveTextContent(examPaperQuestionCustomerAnswer.getQuestionType())) {
                    TextContent content = textContentService.getById(examPaperQuestionCustomerAnswer.getTextContentId());
                    examPaperSubmitItemVM.setContent(content.getContent());
                } else {
                    examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                }
                break;
        }
    }
}
