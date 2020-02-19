package com.question.admin.service.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.exam.question.TextContent;
import com.question.admin.mapper.exam.TextContentMapper;
import com.question.admin.service.exam.TextContentService;
import org.springframework.stereotype.Service;

@Service
public class TextContentServiceImpl extends ServiceImpl<TextContentMapper, TextContent> implements TextContentService {
    public int insert(TextContent textContent){
        return baseMapper.insert(textContent);
    }
}
