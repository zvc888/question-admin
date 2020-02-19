package com.question.admin.web.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.LoginUser;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.exam.ExamPaperAnswer;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.domain.vo.student.exam.ExamPaperReadVM;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitItemVM;
import com.question.admin.domain.vo.student.exam.ExamPaperSubmitVM;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerInfo;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerPageResponseVM;
import com.question.admin.domain.vo.student.paper.ExamPaperAnswerPageVM;
import com.question.admin.domain.vo.student.paper.UserEventLog;
import com.question.admin.service.event.CalculateExamPaperAnswerCompleteEvent;
import com.question.admin.service.exam.ExamPaperAnswerService;
import com.question.admin.service.sysmgr.UserService;
import com.question.admin.utils.DateTimeUtil;
import com.question.admin.utils.ExamUtil;
import com.question.admin.utils.PageInfoHelper;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.exam.Subject;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.PaperService;
import com.question.admin.service.exam.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.question.admin.constant.Constants.SERVER_ERROR;


@RequestMapping(value = "/api/wx/student/paper/answer")
@AllArgsConstructor
@RestController
public class ExamPaperAnswerController extends BaseWXApiController {
private final UserService userService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ApplicationEventPublisher eventPublisher;
    private final SubjectService subjectService;
    private final PaperService paperService;
    @RequestMapping(value = "/answerSubmit", method = RequestMethod.POST)
    public Result<String> answerSubmit(@RequestBody String answer) {
        Map<String, String> map = JSON.parseObject(answer, new TypeReference<Map<String, String>>(){});
//        {"1_5_1":"A","2_6_1":"A","id":"1","doTime":4}
        ExamPaperSubmitVM examPaperSubmitVM = requestToExamPaperSubmitVM(map);
        LoginUser currentUser = UserContext.getCurrentUser();
        User user = userService.findUserByAccount(currentUser.getAccount());
        ExamPaperAnswerInfo examPaperAnswerInfo = examPaperAnswerService.calculateExamPaperAnswer(examPaperSubmitVM,user );
        if (null == examPaperAnswerInfo) {
            Result r = new Result();
            r.setResult(false);
            r.setCode(SERVER_ERROR);
            r.setMessage("试卷不能重复做");
           return r;
        }
        ExamPaperAnswer examPaperAnswer = examPaperAnswerInfo.getExamPaperAnswer();
        Integer userScore = examPaperAnswer.getUserScore();
        String scoreVm = ExamUtil.scoreToVM(userScore);

        UserEventLog userEventLog = new UserEventLog(currentUser.getUserId(), currentUser.getAccount(), currentUser.getName(), new Date());
        String content = currentUser.getName() + " 提交试卷：" + examPaperAnswerInfo.getPaper().getName()
                + " 得分：" + scoreVm
                + " 耗时：" + ExamUtil.secondToVM(examPaperAnswer.getDoTime());
        userEventLog.setContent(content);
        eventPublisher.publishEvent(new CalculateExamPaperAnswerCompleteEvent(examPaperAnswerInfo));
        return Result.getSuccess(scoreVm);
    }

    private ExamPaperSubmitVM requestToExamPaperSubmitVM(Map<String, String> answer) {
        ExamPaperSubmitVM examPaperSubmitVM = new ExamPaperSubmitVM();
        examPaperSubmitVM.setId(Long.valueOf(answer.get("id")));
        examPaperSubmitVM.setDoTime(Integer.valueOf(answer.get("doTime")));
        List<String> parameterNames = answer.keySet().stream()
                .filter(n -> n.contains("_"))
                .collect(Collectors.toList());
        //题目答案按序号分组
        Map<String, List<String>> questionGroup = parameterNames.stream().collect(Collectors.groupingBy(p -> p.substring(0, p.indexOf("_"))));
        List<ExamPaperSubmitItemVM> answerItems = new ArrayList<>();
        questionGroup.forEach((k, v) -> {
            ExamPaperSubmitItemVM examPaperSubmitItemVM = new ExamPaperSubmitItemVM();
            String p = v.get(0);
            String[] keys = p.split("_");
            examPaperSubmitItemVM.setQuestionId(Long.parseLong(keys[1]));
            examPaperSubmitItemVM.setItemOrder(Integer.parseInt(keys[0]));
            QuestionTypeEnum typeEnum = QuestionTypeEnum.fromCode(Integer.parseInt(keys[2]));
            if (v.size() == 1) {
                String content = answer.get(p);
                examPaperSubmitItemVM.setContent(content);
                if (typeEnum == QuestionTypeEnum.MultipleChoice) {
                    examPaperSubmitItemVM.setContentArray(Arrays.asList(content.split(",")));
                }
            } else {  //多个空 填空题
                List<String> answers = v.stream().sorted(Comparator.comparingInt(ExamUtil::lastNum)).map(inputKey -> answer.get(inputKey)).collect(Collectors.toList());
                examPaperSubmitItemVM.setContentArray(answers);
            }
            answerItems.add(examPaperSubmitItemVM);
        });
        examPaperSubmitVM.setAnswerItems(answerItems);
        return examPaperSubmitVM;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<ExamPaperAnswerPageResponseVM>> list(@Valid ExamPaperAnswerPageVM model,
                                                             @RequestParam(defaultValue = "1") int pageNo,
                                                             @RequestParam(defaultValue = "10") int limit) {

        model.setCreatorId(UserContext.getCurrentUser().getUserId());
        Page<ExamPaperAnswer> page = new Page(pageNo, limit);
        QueryWrapper<ExamPaperAnswer> queryWrapper =  new QueryWrapper();
        queryWrapper.eq("creator_id", model.getCreatorId());
        Long subjectId = model.getSubjectId();
        if(null != subjectId) {
            queryWrapper.eq("subject_id", subjectId);
        }
        queryWrapper.orderByDesc("id");
        IPage<ExamPaperAnswer> pageInfo = examPaperAnswerService.page(page, queryWrapper);
        IPage<ExamPaperAnswerPageResponseVM> responsePage = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.getById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setSubjectName(subject.getName());
            vm.setCreatedTime(DateTimeUtil.dateFormat(new Date()));
            return vm;
        });
        Result result = new Result();
        result.setData(responsePage);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    @GetMapping(value = "/read/{id}")
    public Result<ExamPaperReadVM> read(@PathVariable Integer id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(id);
        ExamPaperReadVM vm = new ExamPaperReadVM();
        ExamPaperEditRequestVM paper = paperService.examPaperToVM(examPaperAnswer.getPaperId());
        ExamPaperSubmitVM answer = examPaperAnswerService.examPaperAnswerToVM(examPaperAnswer.getId());
        vm.setPaper(paper);
        vm.setAnswer(answer);
        return Result.getSuccess(vm);
    }
}
