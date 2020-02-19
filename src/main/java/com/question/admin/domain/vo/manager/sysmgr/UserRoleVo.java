package com.question.admin.domain.vo.manager.sysmgr;

import lombok.Data;

import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author zvc
 * @since 2018-12-28
 */
@Data
public class UserRoleVo {
    private Long userId;
    private Set<Long> roleIds;
}
