package com.question.admin.web.wx;

import com.alibaba.fastjson.JSON;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.wx.CodeDTO;
import com.question.admin.service.sysmgr.UserService;
import com.question.admin.utils.ModelMapperSingle;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.student.user.UserResponseVM;
import com.question.admin.domain.vo.student.user.UserUpdateVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class WxAppletController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    private final static ModelMapper modelMapper = ModelMapperSingle.Instance();

    /**
     * 微信小程序端用户登陆api
     * 返回给小程序端 自定义登陆态 token
     */
    @PostMapping("/api/wx/user/login")
    public Result wxAppletLoginApi(@RequestBody @Validated CodeDTO request, HttpServletResponse response) {
        log.info("code:{}", JSON.toJSONString(request));
        return userService.wxUserLogin(request.getCode(), response);
    }

    @GetMapping("/api/wx/user/current")
    public Result current() {
        User user = userService.findUserByAccount(UserContext.getCurrentUser().getAccount());
        UserResponseVM userVm = UserResponseVM.from(user);
        return Result.getSuccess(userVm);
    }


    @PostMapping(value = "/api/wx/user/update")
    public Result<UserResponseVM> update(@Valid @RequestBody UserUpdateVM model) {

        User user = userService.getById(UserContext.getCurrentUser().getUserId());
        modelMapper.map(model, user);
        user.setModifiedTime(new Date());
        user.setEditor(user.getAccount());
        userService.updateById(user);
        UserResponseVM userVm = UserResponseVM.from(user);
        return Result.getSuccess(userVm);
    }

    /**
     * 需要认证的测试接口  需要 @RequiresAuthentication 注解，则调用此接口需要 header 中携带自定义登陆态 authorization
     */
    @RequiresAuthentication
    @PostMapping("/sayHello")
    public ResponseEntity sayHello() {
        Map<String, String> result = new HashMap<>();
        result.put("words", "hello World");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}