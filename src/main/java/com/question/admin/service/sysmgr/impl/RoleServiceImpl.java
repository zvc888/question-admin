package com.question.admin.service.sysmgr.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.sysmgr.RoleAuthority;
import com.question.admin.domain.entity.sysmgr.UserRole;
import com.question.admin.domain.vo.manager.sysmgr.RoleAuth;
import com.question.admin.mapper.sysmgr.RoleMapper;
import com.question.admin.mapper.sysmgr.UserRoleMapper;
import com.question.admin.service.sysmgr.RoleAuthorityService;
import com.question.admin.domain.entity.sysmgr.Role;
import com.question.admin.domain.vo.Result;
import com.question.admin.service.sysmgr.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    protected RoleAuthorityService roleAuthorityService;

    @Autowired
    protected UserRoleMapper userRoleMapper;


    @Override
    public List<Role> findRoleByUserId(Long userId) {
        QueryWrapper<UserRole> userRoleWrapper= new QueryWrapper<>();
        userRoleWrapper.eq("user_id",userId);
        userRoleWrapper.eq("yn_flag","1");
        List<UserRole> userRoleList= userRoleMapper.selectList(userRoleWrapper);

        //查询用户角色
        QueryWrapper<Role> roleWrapper= new QueryWrapper<>();
        userRoleWrapper.eq("yn_flag","1");
        roleWrapper.in("id",userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList()));

        List<Role> roleList= baseMapper.selectList(roleWrapper);
        return roleList;
    }

    @Override
    public Result persist(Role role) {
        Date currentDate= Date.from(Instant.now());
        if(role.getId()!=null){
            role.setEditor(UserContext.getCurrentUser().getAccount());
            role.setModifiedTime(currentDate);
            baseMapper.updateById(role);
        }else{
            role.setYnFlag("1");
            role.setEditor(UserContext.getCurrentUser().getAccount());
            role.setCreator(UserContext.getCurrentUser().getAccount());
            role.setCreatedTime(currentDate);
            role.setModifiedTime(currentDate);
            baseMapper.insert(role);
        }
        return new Result(true,null,null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 修改权限
     * @param roleAuth
     * @return
     */
    @Override
    public Result saveRoleAuths(RoleAuth roleAuth) {
        Date currentDate= Date.from(Instant.now());

        RoleAuthority role= new RoleAuthority();
        role.setRoleId(roleAuth.getRoleId());
        role.setModifiedTime(currentDate);
        roleAuthorityService.deleteAuthByRoleId(role);

        RoleAuthority tempAuth ;
        List<RoleAuthority> authList=new ArrayList<>();
        for(Long authId : roleAuth.getAuthorityIds()){
            tempAuth = new RoleAuthority(roleAuth.getRoleId(),authId);
            tempAuth.setYnFlag("1");
            tempAuth.setEditor(UserContext.getCurrentUser().getAccount());
            tempAuth.setCreator(UserContext.getCurrentUser().getAccount());
            tempAuth.setCreatedTime(currentDate);
            tempAuth.setModifiedTime(currentDate);
            authList.add(tempAuth);
        }
        roleAuthorityService.batchInsert(authList);

        return new Result(true,null,null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 根据角色ID获取权限
     * @param roleId
     * @return
     */
    @Override
    public Result selectAuthByRoleId(Long roleId) {
        List<Long> auths= roleAuthorityService.selectAuthByRoleId(roleId);
        return new Result(true,null,auths, Constants.TOKEN_CHECK_SUCCESS);
    }


}
