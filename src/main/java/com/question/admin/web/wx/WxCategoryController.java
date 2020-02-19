package com.question.admin.web.wx;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.domain.entity.exam.Category;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.CategoryService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/wx/category")
@AllArgsConstructor
public class WxCategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/list")
    public Result list(@RequestParam(required = false) String name,  @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {

        Page<Category> page = new Page(pageNo, limit);

        QueryWrapper<Category> eWrapper = new QueryWrapper();
        eWrapper.eq("status", "1");
        if (StringUtils.isNotEmpty(name)) {
            eWrapper.like("name", name);
        }
        eWrapper.orderByDesc("modified_time");
        IPage<Category> list = categoryService.page(page, eWrapper);
        return Result.getSuccess(list);
    }
}
