package com.question.admin.web.wx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.question.admin.domain.entity.exam.Category;
import com.question.admin.service.exam.FileUpload;
import com.question.admin.domain.entity.exam.SwiperConfig;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.CategoryService;
import com.question.admin.service.exam.PaperService;
import com.question.admin.service.exam.SwiperConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/wx/user/dashboard")
@AllArgsConstructor
public class DashboardController extends BaseWXApiController {

    private final PaperService paperService;

    private final CategoryService categoryService;
    private final SwiperConfigService swiperConfigService;
    private final FileUpload fileUpload;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Result index() {
        QueryWrapper<Category> categoryBean = new QueryWrapper();
        categoryBean.eq("status", 1);
        List<Category> result = categoryService.list(categoryBean);
        return Result.getSuccess(result);
    }

    @RequestMapping(value = "/swiper", method = RequestMethod.GET)
    public Result swiper() {
        QueryWrapper<SwiperConfig> categoryBean = new QueryWrapper();
        categoryBean.eq("status", 1);
        List<SwiperConfig> result = swiperConfigService.list(categoryBean)
                .stream().map(s -> {
                    s.setUrl(fileUpload.getDownloadUrl(s.getUrl()));
                    return s;
                }).collect(Collectors.toList());
        return Result.getSuccess(result);
    }


}
