package com.question.admin.service.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.exam.Subject;
import com.question.admin.mapper.exam.SubjectMapper;
import com.question.admin.service.exam.SubjectService;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
}
