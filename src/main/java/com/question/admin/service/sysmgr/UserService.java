package com.question.admin.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.sysmgr.UserPassword;
import com.question.admin.domain.vo.manager.sysmgr.UserRoleVo;
import com.question.admin.domain.vo.manager.sysmgr.UserVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户账号查询用户详情
     * @param account
     * @return
     */
    User findUserByAccount(String account);

    Result wxUserLogin(String code, HttpServletResponse response) ;

    /**
     * 用户登录
     * @param user
     * @return
     */
    Result login(UserVo user, HttpServletResponse response);

    /**
     * ERP登录
     * @return
     */
    Result loginErp(HttpServletResponse response);

    /**
     * 保存用户
     * @param user
     * @return
     */
    Result persist(User user);

    /**
     * 获取用户ID角色
     * @param userId
     * @return
     */
    Result findUserRole(Long userId);

    /**
     * 修改用户角色
     * @param userRole
     * @return
     */
    Result saveUserRoles(UserRoleVo userRole);

    /**
     * 修改用户密码
     * @param userPassword
     * @return
     */
    Result editPassWord(UserPassword userPassword);

}
