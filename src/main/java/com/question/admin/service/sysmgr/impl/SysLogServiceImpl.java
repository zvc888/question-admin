package com.question.admin.service.sysmgr.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.sysmgr.SysLog;
import com.question.admin.mapper.sysmgr.SysLogMapper;
import com.question.admin.service.sysmgr.SysLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
