package com.question.admin.service.sysmgr;


import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.Resource;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.sysmgr.ResourceNode;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 查询列表
     * @return
     */
    List<ResourceNode> findAll();

    /**
     * 根据用户查询
     * @param userId
     * @return
     */
    List<ResourceNode> findByUserId(Long userId);


    /**
     * 保存
     * @param resource
     * @return
     */
    Result persist(Resource resource);
}
