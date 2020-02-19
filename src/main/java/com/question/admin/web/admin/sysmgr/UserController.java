package com.question.admin.web.admin.sysmgr;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.annontation.SysLogAnnotation;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.sysmgr.UserPassword;
import com.question.admin.domain.vo.manager.sysmgr.UserRoleVo;
import com.question.admin.service.sysmgr.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

import static com.question.admin.constant.SecurityConsts.HIDDEN_PASSWORD;

@Api(tags = "用户")
@RestController
@RequestMapping(value="/sysmgr/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 分页查询
     * @param user
     * @param pageNo
     * @param limit
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.query")
    @ApiOperation("查询用户，并返回用户的数量")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(User user,
                       @RequestParam(defaultValue = "1")int pageNo,
                       @RequestParam(defaultValue = "10")int limit){
        Result result = new Result();
        Page<User> page = new Page(pageNo, limit);
        QueryWrapper<User> eWrapper = new QueryWrapper(user);
        eWrapper.eq("yn_flag","1");
        IPage<User> list = userService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }


    /**
     * 根据Id查询
     * @param user
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.query")
    @RequestMapping(value="/find",method = {RequestMethod.POST})
    public Result findById(@RequestBody User user){
        User userBean= userService.getById(user.getId());
        userBean.setPassword(HIDDEN_PASSWORD);

        Result result = new Result();
        result.setData(userBean);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     * @param user
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.save")
    @RequestMapping(value="/save",method = {RequestMethod.POST})
    public Result save(@RequestBody User user){
        Result r = userService.persist(user);
        return r;
    }

    /**
     * 删除
     * @param user
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.delete")
    @RequestMapping(value="/delete",method = {RequestMethod.POST})
    public Result dropById(@RequestBody User user){
        Result result ;
        if(user.getId()!=null){
            User delUser= new User();
            delUser.setId(user.getId());
            delUser.setYnFlag("0");
            delUser.setEditor(UserContext.getCurrentUser().getAccount());
            delUser.setModifiedTime(Date.from(Instant.now()));
            result=new Result(userService.updateById(delUser),null,null,Constants.TOKEN_CHECK_SUCCESS);
        }else{
            result = new Result(false, "", null ,Constants.PARAMETERS_MISSING);
        }
        return result;
    }

    /**
     * 根据用户Id查询角色
     * @param user
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.query")
    @RequestMapping(value="/findUserRole",method = {RequestMethod.POST})
    public Result findUserRole(@RequestBody UserRoleVo user){
        return userService.findUserRole(user.getUserId());
    }

    /**
     * 更改用户角色
     * @param userRole
     * @return
     */
    @SysLogAnnotation
    @RequiresPermissions("sysmgr.user.save")
    @RequestMapping(value="/saveUserRole",method = {RequestMethod.POST})
    public Result saveUserRole(@RequestBody UserRoleVo userRole){
        return userService.saveUserRoles(userRole);
    }

    /**
     * 修改密码
     * @param userPassword
     * @return
     */
    @SysLogAnnotation
    @RequiresAuthentication
    @RequestMapping(value="/editpassword",method = {RequestMethod.POST})
    public Result editPassWord(@RequestBody UserPassword userPassword){
        return userService.editPassWord(userPassword);
    }
}
