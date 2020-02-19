package com.question.admin.service.sysmgr.impl;

import com.question.admin.domain.entity.sysmgr.LoginLog;
import com.question.admin.mapper.sysmgr.LoginLogMapper;
import com.question.admin.service.sysmgr.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2019-07-26
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
