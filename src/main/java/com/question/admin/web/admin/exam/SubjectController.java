package com.question.admin.web.admin.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.domain.entity.exam.Subject;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Api(description = "科目管理")
@RestController
@RequestMapping("/exam/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "所有类别", notes = "查询所有类别")
    @RequiresPermissions("exam.subject.query")
    @GetMapping(value = "/list")
    public Result list(Subject subject,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {
        boolean hasRole = SecurityUtils.getSubject().hasRole("管理员");

        Result result = new Result();
        Page<Subject> page = new Page(pageNo, limit);
        if (!hasRole) {
            subject.setCreatorId( UserContext.getCurrentUser().getUserId());
        }
        QueryWrapper<Subject> eWrapper = new QueryWrapper(subject);
        eWrapper.eq("status", "1");
        IPage<Subject> list = subjectService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }
    @ApiOperation(value = "所有类别", notes = "所有类别")
    @RequiresPermissions("exam.category.query")
    @GetMapping(value = "/")
    public Result listAll(Subject subject) {
        QueryWrapper<Subject> categoryBean =  new QueryWrapper();
        categoryBean.eq("status", 1);
        Long categoryId = subject.getCategoryId();
        if(null != categoryId) {
            categoryBean.eq("category_id", categoryId);
        }
        List<Subject> list = subjectService.list(categoryBean);
        Result result = new Result();
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }
    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @RequiresPermissions("exam.subject.query")
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public Result findById(@PathVariable Long id) {
        Subject subjectBean = subjectService.getById(id);
        Result result = new Result();
        result.setData(subjectBean);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     *
     * @param subject
     * @return
     */
    @RequiresPermissions("exam.subject.save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Result save(@RequestBody Subject subject) {
        boolean save;
        if (null == subject.getId()) {
            String account = UserContext.getCurrentUser().getAccount();
            subject.setCreatorId(UserContext.getCurrentUser().getUserId());
            subject.setCreator(account);
            subject.setEditor(account);
            Date date = Date.from(Instant.now());
            subject.setCreatedTime(date);
            subject.setModifiedTime(date);
            save  = subjectService.save(subject);
        } else{
            String account = UserContext.getCurrentUser().getAccount();
            subject.setEditor(account);
            Date date = Date.from(Instant.now());
            subject.setModifiedTime(date);
            save = subjectService.updateById(subject);
        }
        return new Result(save, null, null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequiresPermissions("exam.subject.delete")
    @DeleteMapping(value = "/{id}")
    public Result dropById(@PathVariable Long id) {
        Result result;
        if (null != id) {
            Subject delsubject = new Subject();
            delsubject.setId(id);
            delsubject.setStatus(YNFlagStatusEnum.FAIL.getCode());
            delsubject.setEditor(UserContext.getCurrentUser().getAccount());
            delsubject.setModifiedTime(Date.from(Instant.now()));
            result = new Result(subjectService.updateById(delsubject), null, null, Constants.TOKEN_CHECK_SUCCESS);
        } else {
            result = new Result(false, "", null, Constants.PARAMETERS_MISSING);
        }
        return result;
    }

}
