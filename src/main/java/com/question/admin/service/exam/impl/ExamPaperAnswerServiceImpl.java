package com.question.admin.service.exam.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.exam.Question;
import com.question.admin.domain.entity.exam.enums.ExamPaperTypeEnum;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.exam.ExamPaperAnswer;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitItemVM;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitVM;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerInfo;
import com.question.admin.domain.vo.student.paper.ExamPaperQuestionCustomerAnswer;
import com.question.admin.service.exam.*;
import com.question.admin.utils.ExamUtil;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.entity.exam.paper.ExamPaperTitleItemObject;
import com.question.admin.mapper.exam.ExamPaperAnswerMapper;
import com.question.admin.service.exam.enums.ExamPaperAnswerStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExamPaperAnswerServiceImpl extends ServiceImpl<ExamPaperAnswerMapper, ExamPaperAnswer> implements ExamPaperAnswerService {

    private final PaperService paperService;
    private final TextContentService textContentService;
    private final QuestionService questionService;
    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;

    @Override
    public ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user) {
        ExamPaperAnswerInfo examPaperAnswerInfo = new ExamPaperAnswerInfo();
        Date now = new Date();
        Paper paper = this.paperService.getById(examPaperSubmitVM.getId());
        ExamPaperTypeEnum paperTypeEnum = ExamPaperTypeEnum.fromCode(paper.getPaperType());
        if (paperTypeEnum == ExamPaperTypeEnum.Task) {
            QueryWrapper<ExamPaperAnswer> queryWrapper =  new QueryWrapper();
            queryWrapper.eq("paper_id", examPaperSubmitVM.getId());
            queryWrapper.eq("creator_id", user.getId());
            ExamPaperAnswer examPaperAnswer = this.getOne(queryWrapper);
            if (null != examPaperAnswer)
                return null;
        }
        String frameTextContent = textContentService.getById(paper.getTextContentId()).getContent();
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JSON.parseArray(frameTextContent, ExamPaperTitleItemObject.class);
        List<Long> questionIds = examPaperTitleItemObjects.stream().flatMap(t -> t.getQuestionItems().stream().map(q -> q.getId())).collect(Collectors.toList());
        Collection<Question> questions = questionService.listByIds(questionIds);
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> {
                            Question question = questions.stream().filter(tq -> tq.getId().equals(q.getId())).findFirst().get();
                            ExamPaperSubmitItemVM customerQuestionAnswer = examPaperSubmitVM.getAnswerItems().stream()
                                    .filter(tq -> tq.getQuestionId() == q.getId())
                                    .findFirst()
                                    .orElse(null);
                            return ExamPaperQuestionCustomerAnswerFromVM(question, customerQuestionAnswer, paper, q.getItemOrder(), user, now);
                        })
                ).collect(Collectors.toList());

        ExamPaperAnswer examPaperAnswer = ExamPaperAnswerFromVM(examPaperSubmitVM, paper, examPaperQuestionCustomerAnswers, user, now);
        examPaperAnswerInfo.setPaper(paper);
        examPaperAnswerInfo.setExamPaperAnswer(examPaperAnswer);
        examPaperAnswerInfo.setExamPaperQuestionCustomerAnswers(examPaperQuestionCustomerAnswers);
        return examPaperAnswerInfo;
    }

    private ExamPaperQuestionCustomerAnswer ExamPaperQuestionCustomerAnswerFromVM(Question question, ExamPaperSubmitItemVM customerQuestionAnswer, Paper paper, Integer itemOrder, User user, Date now) {
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = new ExamPaperQuestionCustomerAnswer();
        examPaperQuestionCustomerAnswer.setQuestionId(question.getId());
        examPaperQuestionCustomerAnswer.setExamPaperId(paper.getId());
        examPaperQuestionCustomerAnswer.setQuestionScore(question.getScore());
        examPaperQuestionCustomerAnswer.setSubjectId(paper.getSubjectId());
        examPaperQuestionCustomerAnswer.setItemOrder(itemOrder);
        examPaperQuestionCustomerAnswer.setCreateTime(now);
        examPaperQuestionCustomerAnswer.setCreatorId(user.getId());
        examPaperQuestionCustomerAnswer.setQuestionType(question.getQuestionType());
        examPaperQuestionCustomerAnswer.setQuestionTextContentId(question.getInfoTextContentId());
        if (null == customerQuestionAnswer) {
            examPaperQuestionCustomerAnswer.setCustomerScore(0);
        } else {
            setSpecialFromVM(examPaperQuestionCustomerAnswer, question, customerQuestionAnswer);
        }
        return examPaperQuestionCustomerAnswer;
    }
    private void setSpecialFromVM(ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer, Question question, ExamPaperSubmitItemVM customerQuestionAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setDoRight(question.getCorrect().equals(customerQuestionAnswer.getContent()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case MultipleChoice:
                String customerAnswer = ExamUtil.contentToString(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(customerAnswer);
                examPaperQuestionCustomerAnswer.setDoRight(customerAnswer.equals(question.getCorrect()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case GapFilling:
                String correctAnswer = JSON.toJSONString(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(correctAnswer);
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
            default:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
        }
    }
    private ExamPaperAnswer ExamPaperAnswerFromVM(ExamPaperSubmitVM examPaperSubmitVM, Paper paper, List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers, User user, Date now) {
        Integer systemScore = examPaperQuestionCustomerAnswers.stream().mapToInt(a -> a.getCustomerScore()).sum();
        long questionCorrect = examPaperQuestionCustomerAnswers.stream().filter(a -> a.getCustomerScore().equals(a.getQuestionScore())).count();
        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();
        examPaperAnswer.setPaperName(paper.getName());
        examPaperAnswer.setDoTime(examPaperSubmitVM.getDoTime());
        examPaperAnswer.setPaperId(paper.getId());
        examPaperAnswer.setCreatorId(user.getId());
        examPaperAnswer.setCreatedTime(now);
        examPaperAnswer.setSubjectId(paper.getSubjectId());
        examPaperAnswer.setQuestionCount(paper.getQuestionCount());
        examPaperAnswer.setPaperScore(paper.getScore());
        examPaperAnswer.setPaperType(paper.getPaperType());
        examPaperAnswer.setSystemScore(systemScore);
        examPaperAnswer.setUserScore(systemScore);
        examPaperAnswer.setTaskExamId(paper.getTaskExamId());
        examPaperAnswer.setQuestionCorrect((int) questionCorrect);
        boolean needJudge = examPaperQuestionCustomerAnswers.stream().anyMatch(d -> QuestionTypeEnum.needSaveTextContent(d.getQuestionType()));
        if (needJudge) {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.WaitJudge.getCode());
        } else {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        }
        return examPaperAnswer;
    }

    @Override
    public ExamPaperSubmitVM examPaperAnswerToVM(Long id) {
        ExamPaperSubmitVM examPaperSubmitVM = new ExamPaperSubmitVM();
        ExamPaperAnswer examPaperAnswer = this.getById(id);
        examPaperSubmitVM.setId(examPaperAnswer.getId());
        examPaperSubmitVM.setDoTime(examPaperAnswer.getDoTime());
        examPaperSubmitVM.setScore(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()));
        QueryWrapper<ExamPaperQuestionCustomerAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_paper_answer_id", examPaperAnswer.getId());
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperQuestionCustomerAnswerService.list(queryWrapper);
        List<ExamPaperSubmitItemVM> examPaperSubmitItemVMS = examPaperQuestionCustomerAnswers.stream()
                .map(a -> examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(a))
                .collect(Collectors.toList());
        examPaperSubmitVM.setAnswerItems(examPaperSubmitItemVMS);
        return examPaperSubmitVM;
    }

}
