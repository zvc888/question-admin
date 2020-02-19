package com.question.admin.web.admin.sysmgr;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.domain.entity.sysmgr.SysLog;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.sysmgr.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 登录日志 前端控制器
 * </p>
 *
 * @author zvc
 * @since 2019-07-26
 */
@RestController
@RequestMapping("/sysmgr/syslog")
public class SysLogController {

    @Autowired
    SysLogService sysLogService;
    @Autowired
    SysLogService logService;
    /**
     * 分页查询
     * @param sysLog
     * @param pageNo
     * @param limit
     * @return
     */
    @RequiresPermissions("sysmgr.syslog.query")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(SysLog sysLog,
                       @RequestParam(defaultValue = "1")int pageNo,
                       @RequestParam(defaultValue = "10")int limit){
        Result result = new Result();
        Page<SysLog> page = new Page(pageNo, limit);
//        IPage<SysLog> list = sysLogService.findPage(page, sysLog);
//        result.setData(list);
//        result.setResult(true);
//        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
//        return result;
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper(sysLog);
        queryWrapper.orderByDesc("visit_time");

        IPage<SysLog> list = logService.page(page, queryWrapper);
        return Result.getSuccess(list);
    }

    /**
     * 根据Id查询
     * @param sysLog
     * @return
     */
    @RequiresPermissions("sysmgr.syslog.query")
    @RequestMapping(value="/find",method = {RequestMethod.POST})
    public Result findById(@RequestBody SysLog sysLog){
//        SysLog userBean= sysLogService.findById(sysLog.getId());
//        Result result = new Result();
//        result.setData(userBean);
//        result.setResult(true);
//        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
//        return result;
        SysLog bean = logService.getById(sysLog.getId());
        return Result.getSuccess(bean);
    }

}
