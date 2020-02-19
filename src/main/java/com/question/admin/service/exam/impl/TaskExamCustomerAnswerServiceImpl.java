package com.question.admin.service.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.vo.student.paper.TaskExamCustomerAnswer;
import com.question.admin.mapper.exam.TaskExamCustomerAnswerMapper;
import com.question.admin.service.exam.TaskExamCustomerAnswerService;
import org.springframework.stereotype.Service;

@Service
public class TaskExamCustomerAnswerServiceImpl extends ServiceImpl<TaskExamCustomerAnswerMapper, TaskExamCustomerAnswer> implements TaskExamCustomerAnswerService {
}
