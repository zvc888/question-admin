package com.question.admin.service.exam.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.exam.FeedBack;
import com.question.admin.mapper.exam.FeedBackMapper;
import com.question.admin.service.exam.FeedBackService;
import org.springframework.stereotype.Service;

@Service
public class FeedBackServiceImpl  extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {
}
