package com.question.admin.service.exam.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.Question;
import com.question.admin.domain.entity.exam.enums.ExamPaperTypeEnum;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;
import com.question.admin.domain.vo.manager.exam.question.QuestionEditRequestVM;
import com.question.admin.mapper.exam.PaperMapper;
import com.question.admin.service.exam.TextContentService;
import com.question.admin.utils.ExamUtil;
import com.question.admin.utils.ModelMapperSingle;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.entity.exam.paper.ExamPaperQuestionItemObject;
import com.question.admin.domain.entity.exam.paper.ExamPaperTitleItemObject;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperTitleItemVM;
import com.question.admin.service.exam.PaperService;
import com.question.admin.service.exam.QuestionService;
import com.question.admin.service.exam.enums.ActionEnum;
import com.question.admin.utils.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();

    @Autowired
    private TextContentService textContentService;
    @Autowired
    private QuestionService questionService;


    @Override
    public ExamPaperEditRequestVM examPaperToVM(Long id) {
        Paper examPaper = baseMapper.selectById(id);
        ExamPaperEditRequestVM vm = modelMapper.map(examPaper, ExamPaperEditRequestVM.class);
        TextContent frameTextContent = textContentService.getById(examPaper.getTextContentId());
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JSON.parseArray(frameTextContent.getContent(), ExamPaperTitleItemObject.class);
        List<Long> questionIds = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> q.getId()))
                .collect(Collectors.toList());
        QueryWrapper<Question> questionWrapper = new QueryWrapper<>();
        questionWrapper.in("id", questionIds);
        List<Question> questions = questionService.list(questionWrapper);
        List<ExamPaperTitleItemVM> examPaperTitleItemVMS = examPaperTitleItemObjects.stream().map(t -> {
            ExamPaperTitleItemVM tTitleVM = modelMapper.map(t, ExamPaperTitleItemVM.class);
            List<QuestionEditRequestVM> questionItemsVM = t.getQuestionItems().stream().map(i -> {
                Stream<Question> questionStream = questions.stream().filter(q -> q.getId().equals(i.getId()));
                Question question = questionStream.findFirst().get();
                QuestionEditRequestVM questionEditRequestVM = questionService.getQuestionEditRequestVM(question);
                questionEditRequestVM.setItemOrder(i.getItemOrder());
                return questionEditRequestVM;
            }).collect(Collectors.toList());
            tTitleVM.setQuestionItems(questionItemsVM);
            return tTitleVM;
        }).collect(Collectors.toList());
        vm.setTitleItems(examPaperTitleItemVMS);
        vm.setScore(ExamUtil.scoreToVM(examPaper.getScore()));
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateFormat(examPaper.getLimitStartTime()), DateTimeUtil.dateFormat(examPaper.getLimitEndTime()));
            vm.setLimitDateTime(limitDateTime);
        }
        return vm;
    }

    @Override
    public Paper savePaperFromVM(@Valid ExamPaperEditRequestVM examPaperEditRequestVM) {
        ActionEnum actionEnum = (examPaperEditRequestVM.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        Date now = new Date();
        List<ExamPaperTitleItemVM> titleItemsVM = examPaperEditRequestVM.getTitleItems();
        List<ExamPaperTitleItemObject> frameTextContentList = frameTextContentFromVM(titleItemsVM);
        String frameTextContentStr = JSON.toJSONString(frameTextContentList);

        Paper examPaper;
        if (actionEnum == ActionEnum.ADD) {
            Long userId = UserContext.getCurrentUser().getUserId();
            examPaper = modelMapper.map(examPaperEditRequestVM, Paper.class);
            TextContent frameTextContent = new TextContent(frameTextContentStr, now,userId);
            textContentService.insert(frameTextContent);
            examPaper.setTextContentId(frameTextContent.getId());
            examPaper.setCreatedTime(now);
            examPaper.setModifiedTime(now);
            examPaper.setCreatorId(UserContext.getCurrentUser().getUserId());
            examPaper.setCreator(UserContext.getCurrentUser().getAccount());
            examPaper.setEditor(UserContext.getCurrentUser().getAccount());
            examPaper.setStatus("1");
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            baseMapper.insert(examPaper);
        } else {
            examPaper = baseMapper.selectById(examPaperEditRequestVM.getId());
            TextContent frameTextContent = textContentService.getById(examPaper.getTextContentId());
            frameTextContent.setContent(frameTextContentStr);
            textContentService.updateById(frameTextContent);
            modelMapper.map(examPaperEditRequestVM, examPaper);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaper.setEditor(UserContext.getCurrentUser().getAccount());
            baseMapper.updateById(examPaper);
        }
        return examPaper;
    }

    private void examPaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, Paper examPaper, List<ExamPaperTitleItemVM> titleItemsVM) {
        Integer questionCount = titleItemsVM.stream()
                .mapToInt(t -> t.getQuestionItems().size()).sum();
        Integer score = titleItemsVM.stream().
                flatMapToInt(t -> t.getQuestionItems().stream()
                        .mapToInt(q -> ExamUtil.scoreFromVM(q.getScore()))
                ).sum();
        examPaper.setQuestionCount(questionCount);
        examPaper.setScore(score);
        List<String> dateTimes = examPaperEditRequestVM.getLimitDateTime();
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            examPaper.setLimitStartTime(DateTimeUtil.parse(dateTimes.get(0), DateTimeUtil.STANDER_FORMAT));
            examPaper.setLimitEndTime(DateTimeUtil.parse(dateTimes.get(1), DateTimeUtil.STANDER_FORMAT));
        }
    }

    private List<ExamPaperTitleItemObject> frameTextContentFromVM(List<ExamPaperTitleItemVM> titleItems) {
        AtomicInteger index = new AtomicInteger(1);
        return titleItems.stream().map(t -> {
            ExamPaperTitleItemObject titleItem = modelMapper.map(t, ExamPaperTitleItemObject.class);
            List<ExamPaperQuestionItemObject> questionItems = t.getQuestionItems().stream()
                    .map(q -> {
                        ExamPaperQuestionItemObject examPaperQuestionItemObject = modelMapper.map(q, ExamPaperQuestionItemObject.class);
                        examPaperQuestionItemObject.setItemOrder(index.getAndIncrement());
                        return examPaperQuestionItemObject;
                    })
                    .collect(Collectors.toList());
            titleItem.setQuestionItems(questionItems);
            return titleItem;
        }).collect(Collectors.toList());
    }
}
