package com.question.admin.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.vo.manager.sysmgr.RoleAuth;
import com.question.admin.domain.entity.sysmgr.Role;
import com.question.admin.domain.vo.Result;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId
     * @return
     */
    List<Role> findRoleByUserId(Long userId);

    /**
     * 保存角色
     *
     * @param role
     * @return
     */
    Result persist(Role role);

    /**
     * 修改权限
     * @param roleAuth
     * @return
     */
    Result saveRoleAuths(RoleAuth roleAuth);

    /**
     * 根据角色ID获取权限
     * @param roleId
     * @return
     */
    Result selectAuthByRoleId(Long roleId);


}
