package com.question.admin.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.Authority;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.sysmgr.AuthorityNode;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
public interface AuthorityService extends IService<Authority> {

    /**
     * 查询列表
     * @return
     */
    List<AuthorityNode> findAll();

    /**
     * 根据用户查询
     * @param userId
     * @return
     */
    List<Object> findByUserId(Long userId);

    /**
     * 保存
     * @param resource
     * @return
     */
    Result persist(Authority resource);
}
