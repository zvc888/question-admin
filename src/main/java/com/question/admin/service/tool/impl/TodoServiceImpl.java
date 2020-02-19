package com.question.admin.service.tool.impl;

import com.question.admin.service.tool.TodoService;
import com.question.admin.domain.entity.tool.Todo;
import com.question.admin.mapper.tool.TodoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 待办事项 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2019-08-14
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

}
