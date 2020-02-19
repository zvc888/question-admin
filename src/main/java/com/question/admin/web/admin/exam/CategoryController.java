package com.question.admin.web.admin.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.domain.entity.exam.Category;
import com.question.admin.constant.Constants;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Api(description = "类别管理")
@RestController
@RequestMapping("/exam/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "分页所有类别", notes = "分页查询所有类别")
    @RequiresPermissions("exam.category.query")
    @GetMapping(value = "/list")
    public Result list(Category category,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {
        boolean hasRole = SecurityUtils.getSubject().hasRole("管理员");

        Result result = new Result();
        Page<Category> page = new Page(pageNo, limit);
        if (!hasRole) {
            category.setCreatorId( UserContext.getCurrentUser().getUserId());
        }

        QueryWrapper<Category> eWrapper = new QueryWrapper(category);
        eWrapper.eq("status", "1");
        String name = category.getName();
        if (StringUtils.isNotEmpty(name)) {
            eWrapper.like("name",name);
            category.setName(null);
        }

        IPage<Category> list = categoryService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }
    @ApiOperation(value = "所有类别", notes = "所有类别")
    @RequiresPermissions("exam.category.query")
    @GetMapping(value = "/")
    public Result listAll() {
        QueryWrapper<Category> categoryBean =  new QueryWrapper();
        categoryBean.eq("status", 1);
        List<Category> list = categoryService.list(categoryBean);
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
    @RequiresPermissions("exam.category.query")
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public Result findById(@PathVariable Long id) {
        Category categoryBean = categoryService.getById(id);
        Result result = new Result();
        result.setData(categoryBean);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     *
     * @param category
     * @return
     */
    @RequiresPermissions("exam.category.save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public Result save(@RequestBody Category category) {
        boolean save;
        if (null == category.getId()) {
            Long uerId = UserContext.getCurrentUser().getUserId();
            category.setCreatorId(uerId);
            String account = UserContext.getCurrentUser().getAccount();
            category.setCreator(account);
            category.setEditor(account);
            Date date = Date.from(Instant.now());
            category.setCreatedTime(date);
            category.setModifiedTime(date);
            save  = categoryService.save(category);
        } else{
            String account = UserContext.getCurrentUser().getAccount();
            category.setEditor(account);
            Date date = Date.from(Instant.now());
            category.setModifiedTime(date);
            save = categoryService.updateById(category);
        }
        return new Result(save, null, null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequiresPermissions("exam.category.delete")
    @DeleteMapping(value = "/{id}")
    public Result dropById(@PathVariable Long id) {
        Result result;
        if (null != id) {
            Category delCategory = new Category();
            delCategory.setId(id);
            delCategory.setStatus(YNFlagStatusEnum.FAIL.getCode());
            delCategory.setEditor(UserContext.getCurrentUser().getAccount());
            delCategory.setModifiedTime(Date.from(Instant.now()));
            result = new Result(categoryService.updateById(delCategory), null, null, Constants.TOKEN_CHECK_SUCCESS);
        } else {
            result = new Result(false, "", null, Constants.PARAMETERS_MISSING);
        }
        return result;
    }

}
