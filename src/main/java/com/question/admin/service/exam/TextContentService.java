package com.question.admin.service.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.exam.question.TextContent;

public interface TextContentService  extends IService<TextContent> {
    int insert(TextContent textContent);
}
