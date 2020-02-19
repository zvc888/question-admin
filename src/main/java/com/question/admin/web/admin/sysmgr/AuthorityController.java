package com.question.admin.web.admin.sysmgr;

import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.sysmgr.Authority;
import com.question.admin.domain.vo.manager.sysmgr.AuthorityNode;
import com.question.admin.service.sysmgr.AuthorityService;
import com.question.admin.domain.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Api(description = "权限接口")
@RestController
@RequestMapping(value="/sysmgr/authority")
public class AuthorityController {
    @Autowired
    AuthorityService authorityService;

    /**
     * 查询所有权限
     * @return
     */
    @ApiOperation(value = "所有权限" ,  notes="查询所有权限")
    @RequiresPermissions("sysmgr.authority.query")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(){
        List<AuthorityNode> trees = authorityService.findAll();

        Result result = new Result();
        result.setData(trees);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     * @param authority
     * @return
     */
    @RequiresPermissions("sysmgr.authority.save")
    @RequestMapping(value="/save",method = {RequestMethod.POST})
    public Result save(@RequestBody Authority authority){
        return authorityService.persist(authority);
    }

    /**
     * 删除
     * @param authority
     * @return
     */
    @RequiresPermissions("sysmgr.authority.delete")
    @RequestMapping(value="/delete",method = {RequestMethod.POST})
    public Result dropById(@RequestBody Authority authority){
        Result result ;
        if(authority.getId()!=null){
            Authority delAuth= new Authority();
            delAuth.setId(authority.getId());
            delAuth.setYnFlag("0");
            delAuth.setEditor(UserContext.getCurrentUser().getAccount());
            delAuth.setModifiedTime(Date.from(Instant.now()));
            result=new Result(authorityService.updateById(delAuth),null,null,Constants.TOKEN_CHECK_SUCCESS);
        }else{
            result = new Result(false, "", null ,Constants.PARAMETERS_MISSING);
        }
        return result;
    }
}
