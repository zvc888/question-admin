package com.question.admin.web.admin.exam;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.Question;
import com.question.admin.domain.entity.exam.question.QuestionObject;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.domain.vo.manager.exam.question.QuestionEditRequestVM;
import com.question.admin.domain.vo.manager.exam.question.QuestionResponseVM;
import com.question.admin.domain.vo.manager.exam.question.QuestionTypeEnum;
import com.question.admin.utils.*;
import com.question.admin.constant.Constants;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.QuestionService;
import com.question.admin.service.exam.TextContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

import static com.question.admin.constant.Constants.PARAMETERS_VALID_ERROR;
import static com.question.admin.domain.vo.Result.SUCCESS_CODE;

@Api(description = "题目管理")
@RestController
@RequestMapping("/exam/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextContentService textContentService;

    @ApiOperation(value = "所有题目", notes = "查询所有题目")
    @RequiresPermissions("exam.question.query")
    @GetMapping(value = "/list")
    public Result list(Question question,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {
        boolean hasRole = SecurityUtils.getSubject().hasRole("管理员");

        Page<Question> page = new Page(pageNo, limit);
        if (!hasRole) {
            question.setCreatorId( UserContext.getCurrentUser().getUserId());
        }
        QueryWrapper<Question> eWrapper = new QueryWrapper(question);
        eWrapper.eq("status", "1");
        eWrapper.orderByDesc("created_time");
        IPage<Question> list = questionService.page(page, eWrapper);
        ModelMapper modelMapper = ModelMapperSingle.Instance();
        IPage<QuestionResponseVM> response = PageInfoHelper.copyMap(list, q -> {
            QuestionResponseVM vm = modelMapper.map(q, QuestionResponseVM.class);
            vm.setScore(ExamUtil.scoreToVM(q.getScore()));
            TextContent textContent = textContentService.getById(q.getInfoTextContentId());
            QuestionObject questionObject = JSON.parseObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            return vm;
        });

        return Result.getSuccess(response);
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @RequiresPermissions("exam.question.query")
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public Result findById(@PathVariable Long id) {
//        Question questionBean = questionService.getById(id);
//        Result result = new Result();
//        result.setData(questionBean);
//        result.setResult(true);
//        result.setCode(TOKEN_CHECK_SUCCESS);
//        return result;
        QuestionEditRequestVM newVM = questionService.getQuestionEditRequestVM(id);
        return Result.getSuccess( newVM);
    }

    /**
     * 保存
     *
     * @param model
     * @return
     */
    @RequiresPermissions("exam.question.save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Result save(@RequestBody @Valid QuestionEditRequestVM model) {
//        boolean save;
//        if (null == question.getId()) {
//            String account = UserContext.getCurrentUser().getAccount();
//            question.setCreater(account);
//            question.setEditor(account);
//            Date date = Date.from(Instant.now());
//            question.setCreatedTime(date);
//            question.setModifiedTime(date);
//            save  = questionService.save(question);
//        } else{
//            String account = UserContext.getCurrentUser().getAccount();
//            question.setEditor(account);
//            Date date = Date.from(Instant.now());
//            question.setModifiedTime(date);
//            save = questionService.updateById(question);
//        }
//        return new Result(save, null, null, Constants.TOKEN_CHECK_SUCCESS);

        Result validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if (validQuestionEditRequestResult.getCode() != SUCCESS_CODE) {
            return validQuestionEditRequestResult;
        }
        Question question;
        if (null == model.getId()) {
            question = questionService.insertFullQuestion(model);
        } else {
            question = questionService.updateFullQuestion(model);
        }
        QuestionEditRequestVM newVM = questionService.getQuestionEditRequestVM(question.getId());
        return Result.getSuccess(newVM);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequiresPermissions("exam.question.delete")
    @DeleteMapping(value = "/{id}")
    public Result dropById(@PathVariable Long id) {
        Result result;
        if (null != id) {
            Question delquestion = new Question();
            delquestion.setId(id);
            delquestion.setStatus(0);
            delquestion.setEditor(UserContext.getCurrentUser().getAccount());
            delquestion.setModifiedTime(Date.from(Instant.now()));
            result = Result.getSuccess(questionService.updateById(delquestion));
        } else {
            result = new Result(false, "", null, Constants.PARAMETERS_MISSING);
        }
        return result;
    }
    private Result validQuestionEditRequestVM(QuestionEditRequestVM model) {
        int qType = model.getQuestionType().intValue();
        boolean requireCorrect = qType == QuestionTypeEnum.SingleChoice.getCode() || qType == QuestionTypeEnum.TrueFalse.getCode();
        if (requireCorrect) {
            if (StringUtils.isBlank(model.getCorrect())) {
                String errorMsg = ErrorUtil.parameterErrorFormat("correct", "不能为空");
                return new Result<String>(false, errorMsg, null,Constants.PARAMETERS_MISSING);
            }
        }

        if (qType == QuestionTypeEnum.GapFilling.getCode()) {
            Integer fillSumScore = model.getItems().stream().mapToInt(d -> ExamUtil.scoreFromVM(d.getScore())).sum();
            Integer questionScore = ExamUtil.scoreFromVM(model.getScore());
            if (!fillSumScore.equals(questionScore)) {
                String errorMsg = ErrorUtil.parameterErrorFormat("score", "空分数和与题目总分不相等");
                return new Result<>(false,errorMsg, null, PARAMETERS_VALID_ERROR);
            }
        }
        return Result.getSuccess(null);
    }
}
