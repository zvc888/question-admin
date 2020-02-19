package com.question.admin.web.admin.operation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.exam.FeedBack;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.FeedBackService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping(value = "/operation/feedback")
@AllArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    /**
     * 保存
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @RequiresPermissions("operation.feedback.save")
    public Result save(@RequestBody @Valid FeedBack model) {
//        model.setStatus(1);
        Date now = Date.from(Instant.now());
        model.setModifiedTime(now);
//        model.setCreatedTime(now);
        Long userId = UserContext.getCurrentUser().getUserId();
        model.setEditorId(userId);
        boolean save = feedBackService.updateById(model);
        return Result.getSuccess(save);
    }

    /**
     * 查询
     *
     * @return
     */
    @ApiOperation(value = "所有", notes = "查询所有")
    @RequiresPermissions("operation.feedback.query")
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public Result list(FeedBack feedBack,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {
        Page<FeedBack> page = new Page(pageNo, limit);
        QueryWrapper<FeedBack> eWrapper = new QueryWrapper(feedBack);
        eWrapper.orderByDesc("created_time");
        IPage<FeedBack> list = feedBackService.page(page, eWrapper);
        return Result.getSuccess(list);
    }

    /**
     * 根据Id查询
     *
     * @param
     * @return
     */
    @RequiresPermissions("operation.feedback.query")
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public Result findById(@PathVariable Long id) {
        FeedBack bean = feedBackService.getById(id);

        return Result.getSuccess(bean);
    }
}