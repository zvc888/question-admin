package com.question.admin.web.admin.exam;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.service.exam.FileUpload;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.exam.SwiperConfig;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.exam.SwiperConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "滚动栏管理")
@RestController
@RequestMapping("/exam/swiper")
@Slf4j
public class SwiperConfigController {

    @Autowired
    private SwiperConfigService swiperConfigService;

    @Autowired
    private FileUpload fileUpload;


    @ApiOperation(value = "所有Swiper", notes = "查询所有Swiper")
    @RequiresPermissions("exam.swiper.query")
    @GetMapping(value = "/list")
    public Result list(SwiperConfig model,
                       @RequestParam(defaultValue = "1") int pageNo,
                       @RequestParam(defaultValue = "10") int limit) {

        Page<SwiperConfig> page = new Page(pageNo, limit);
        model.setStatus(1);
        QueryWrapper<SwiperConfig> eWrapper = new QueryWrapper(model);
        eWrapper.orderByDesc("modified_time");
        IPage<SwiperConfig> list = swiperConfigService.page(page, eWrapper);
        List<SwiperConfig> swiperConfigList = list.getRecords().stream().map(s -> {
            s.setUrl(fileUpload.getDownloadUrl(s.getUrl()));
            return s;
        }).collect(Collectors.toList());
        list.setRecords(swiperConfigList);
        return Result.getSuccess(list);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions("exam.swiper.save")
    public Result<Boolean> edit(@RequestBody @Valid SwiperConfig model) {
        Long id = model.getId();
        SwiperConfig swiperConfig = swiperConfigService.getById(id);
        Date currentDate = Date.from(Instant.now());
        if (null == swiperConfig) {
            model.setCreatedTime(currentDate);
            model.setModifiedTime(currentDate);
            boolean flag = swiperConfigService.save(model);

            log.info("add swiper result:[{}]", flag);
            return Result.getSuccess(flag);
        }
        swiperConfig.setModifiedTime(currentDate);
        swiperConfig.setUrl(model.getUrl());
        swiperConfig.setStatus(model.getStatus());
        swiperConfig.setOrderItem(model.getOrderItem());

        boolean flag = swiperConfigService.updateById(swiperConfig);
        return Result.getSuccess(flag);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("exam.swiper.query")
    public Result<SwiperConfig> select(@PathVariable Long id) {
        SwiperConfig swiperConfig = swiperConfigService.getById(id);
        return Result.getSuccess(swiperConfig);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @RequiresPermissions("exam.swiper.delete")
    public Result<Boolean> delete(@PathVariable Long id) {
        SwiperConfig swiperConfig = swiperConfigService.getById(id);
        if (null == swiperConfig) {
            Result result = new Result(false, null, null, Constants.PASSWORD_CHECK_INVALID);
            return result;
        }
        swiperConfig.setStatus(0);
        swiperConfig.setModifiedTime(Date.from(Instant.now()));
        boolean flag = swiperConfigService.updateById(swiperConfig);
        return Result.getSuccess(flag);
    }


    /**
     * 上传文件
     *
     * @return
     */
    @RequiresPermissions("exam.swiper.save")
    @PostMapping(value = "/upload")
    public Result upload(HttpServletRequest request) {
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
            long attachSize = multipartFile.getSize();
            String imgName = multipartFile.getOriginalFilename();
            String filePath;
            try (InputStream inputStream = multipartFile.getInputStream()) {
                filePath = fileUpload.uploadFile(inputStream, attachSize, imgName);
            }
            return Result.getSuccess(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        Result result = new Result(false, null, null, Constants.SERVER_ERROR);
        return result;
    }
}
