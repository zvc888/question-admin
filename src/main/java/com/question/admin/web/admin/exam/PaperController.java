package com.question.admin.web.admin.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.vo.manager.exam.pape.ExamPaperEditRequestVM;
import com.question.admin.service.exam.TextContentService;
import com.question.admin.domain.entity.exam.Paper;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.PaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "试卷管理")
@RestController
@RequestMapping("/exam/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;
    @Autowired
    private TextContentService textContentService;

    @ApiOperation(value = "所有试卷", notes = "查询所有试卷")
    @RequiresPermissions("exam.paper.query")
    @GetMapping(value = "/list")
    public Result list(Paper model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {
        boolean hasRole = SecurityUtils.getSubject().hasRole("管理员");

        Page<Paper> page = new Page(pageNo, limit);
        if (!hasRole) {
            model.setCreatorId(UserContext.getCurrentUser().getUserId());
        }
        QueryWrapper<Paper> eWrapper = new QueryWrapper(model);
        IPage<Paper> list = paperService.page(page, eWrapper);

        return Result.getSuccess(list);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<ExamPaperEditRequestVM> edit(@RequestBody @Valid ExamPaperEditRequestVM model) {
        Paper examPaper = paperService.savePaperFromVM(model);
        ExamPaperEditRequestVM newVM = paperService.examPaperToVM(examPaper.getId());
        return Result.getSuccess(newVM);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result<ExamPaperEditRequestVM> select(@PathVariable Long id) {
        ExamPaperEditRequestVM vm = paperService.examPaperToVM(id);
        return Result.getSuccess(vm);
    }
}
