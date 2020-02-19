package com.question.admin.service.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.enums.ExamPaperTypeEnum;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerInfo;
import com.question.admin.domain.vo.student.paper.ExamPaperQuestionCustomerAnswer;
import com.question.admin.domain.vo.student.paper.TaskExamCustomerAnswer;
import com.question.admin.service.exam.ExamPaperAnswerService;
import com.question.admin.service.exam.ExamPaperQuestionCustomerAnswerService;
import com.question.admin.service.exam.TaskExamCustomerAnswerService;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.vo.manager.exam.ExamPaperAnswer;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.service.event.CalculateExamPaperAnswerCompleteEvent;
import com.question.admin.service.exam.TextContentService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Component
@AllArgsConstructor
public class CalculateExamPaperAnswerListener implements ApplicationListener<CalculateExamPaperAnswerCompleteEvent> {


    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final TextContentService textContentService;
    private final TaskExamCustomerAnswerService examCustomerAnswerService;

    @Override
    @Transactional
    public void onApplicationEvent(CalculateExamPaperAnswerCompleteEvent calculateExamPaperAnswerCompleteEvent) {
        Date now = new Date();

        ExamPaperAnswerInfo examPaperAnswerInfo = (ExamPaperAnswerInfo) calculateExamPaperAnswerCompleteEvent.getSource();
        Paper examPaper = examPaperAnswerInfo.getPaper();
        ExamPaperAnswer examPaperAnswer = examPaperAnswerInfo.getExamPaperAnswer();
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperAnswerInfo.getExamPaperQuestionCustomerAnswers();

        examPaperAnswerService.save(examPaperAnswer);
        examPaperQuestionCustomerAnswers.stream().filter(a -> QuestionTypeEnum.needSaveTextContent(a.getQuestionType())).forEach(d -> {
            TextContent textContent = new TextContent(d.getAnswer(), now, UserContext.getCurrentUser().getUserId());
            textContentService.insert(textContent);
            d.setTextContentId(textContent.getId());
            d.setAnswer(null);
        });
        examPaperQuestionCustomerAnswers.forEach(d -> {
            d.setExamPaperAnswerId(examPaperAnswer.getId());
        });
        examPaperQuestionCustomerAnswerService.saveBatch(examPaperQuestionCustomerAnswers);

        switch (ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            case Task: {
                Long taskId = examPaper.getTaskExamId();
                Long userId = examPaperAnswer.getCreatorId();
                QueryWrapper<TaskExamCustomerAnswer> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("task_id", taskId);
                queryWrapper.eq("creator_id", userId);
                TaskExamCustomerAnswer taskExamCustomerAnswer = examCustomerAnswerService.getOne(queryWrapper);
                examCustomerAnswerService.saveOrUpdate(taskExamCustomerAnswer);
                break;
            }
            default:
                break;
        }
    }
}
