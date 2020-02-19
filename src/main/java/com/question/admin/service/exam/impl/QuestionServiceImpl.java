package com.question.admin.service.exam.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.config.shiro.LoginUser;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.Question;
import com.question.admin.domain.entity.exam.question.QuestionItemObject;
import com.question.admin.domain.entity.exam.question.QuestionObject;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.domain.vo.manager.exam.question.QuestionEditRequestVM;
import com.question.admin.domain.vo.manager.exam.question.QuestionStatusEnum;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.service.exam.QuestionService;
import com.question.admin.service.exam.TextContentService;
import com.question.admin.utils.ExamUtil;
import com.question.admin.utils.ModelMapperSingle;
import com.question.admin.domain.vo.manager.exam.question.QuestionEditItemVM;
import com.question.admin.mapper.exam.QuestionMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    @Autowired
    private TextContentService textContentService;
    @Override
    @Transactional
    public Question insertFullQuestion(QuestionEditRequestVM model) {
        Date now = new Date();

        //题干、解析、选项等 插入
        TextContent infoTextContent = new TextContent();
        infoTextContent.setCreatedTime(now);
        setQuestionInfoFromVM(infoTextContent, model);
        infoTextContent.setCreatorId(UserContext.getCurrentUser().getUserId());
        textContentService.insert(infoTextContent);

        Question question = new Question();
        question.setSubjectId(model.getSubjectId());
        question.setCreatedTime(now);
        question.setQuestionType(model.getQuestionType());
        question.setStatus(QuestionStatusEnum.OK.getCode());
        question.setCorrectFromVM(model.getCorrect(), model.getCorrectArray());
        question.setScore(ExamUtil.scoreFromVM(model.getScore()));
        question.setDifficult(model.getDifficult());
        question.setInfoTextContentId(infoTextContent.getId());
        LoginUser currentUser = UserContext.getCurrentUser();
        question.setCreator(currentUser.getAccount());
        question.setCreatorId(currentUser.getUserId());
        question.setStatus(QuestionStatusEnum.OK.getCode());
        baseMapper.insert(question);
        return question;
    }

    @Override
    public Question updateFullQuestion(QuestionEditRequestVM model) {

        Question question = baseMapper.selectById(model.getId());
        question.setSubjectId(model.getSubjectId());
        question.setScore(ExamUtil.scoreFromVM(model.getScore()));
        question.setDifficult(model.getDifficult());
        question.setCorrectFromVM(model.getCorrect(), model.getCorrectArray());
        baseMapper.updateById(question);

        //题干、解析、选项等 更新
        TextContent infoTextContent = textContentService.getById(question.getInfoTextContentId());
        setQuestionInfoFromVM(infoTextContent, model);
        textContentService.updateById(infoTextContent);

        return question;
    }
    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Long questionId) {
        //题目映射
        Question question = baseMapper.selectById(questionId);
        return getQuestionEditRequestVM(question);
    }

    @Override
    public QuestionEditRequestVM getQuestionEditRequestVM(Question question) {
        //题目映射
        TextContent questionInfoTextContent = textContentService.getById(question.getInfoTextContentId());
        QuestionObject questionObject = JSON.parseObject(questionInfoTextContent.getContent(), QuestionObject.class);
        QuestionEditRequestVM questionEditRequestVM = modelMapper.map(question, QuestionEditRequestVM.class);
        questionEditRequestVM.setTitle(questionObject.getTitleContent());

        //答案
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(question.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                questionEditRequestVM.setCorrect(question.getCorrect());
                break;
            case MultipleChoice:
                questionEditRequestVM.setCorrectArray(ExamUtil.contentToArray(question.getCorrect()));
                break;
            case GapFilling:
                List<String> correctContent = questionObject.getQuestionItemObjects().stream().map(d -> d.getContent()).collect(Collectors.toList());
                questionEditRequestVM.setCorrectArray(correctContent);
                break;
            case ShortAnswer:
                questionEditRequestVM.setCorrect(questionObject.getCorrect());
                break;
            default:
                break;
        }
        questionEditRequestVM.setScore(ExamUtil.scoreToVM(question.getScore()));
        questionEditRequestVM.setAnalyze(questionObject.getAnalyze());


        //题目项映射
        List<QuestionEditItemVM> editItems = questionObject.getQuestionItemObjects().stream().map(o -> {
            QuestionEditItemVM questionEditItemVM = modelMapper.map(o, QuestionEditItemVM.class);
            if (o.getScore() != null) {
                questionEditItemVM.setScore(ExamUtil.scoreToVM(o.getScore()));
            }
            return questionEditItemVM;
        }).collect(Collectors.toList());
        questionEditRequestVM.setItems(editItems);
        return questionEditRequestVM;
    }
    public void setQuestionInfoFromVM(TextContent infoTextContent, QuestionEditRequestVM model) {
        List<QuestionItemObject> itemObjects = model.getItems().stream().map(i ->
                {
                    QuestionItemObject item = modelMapper.map(i, QuestionItemObject.class);
                    item.setScore(ExamUtil.scoreFromVM(i.getScore()));
                    return item;
                }
        ).collect(Collectors.toList());
        QuestionObject questionObject = new QuestionObject();
        questionObject.setQuestionItemObjects(itemObjects);
        questionObject.setAnalyze(model.getAnalyze());
        questionObject.setTitleContent(model.getTitle());
        questionObject.setCorrect(model.getCorrect());
        infoTextContent.setContent(JSON.toJSONString(questionObject));
    }
}
