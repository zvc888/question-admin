package com.question.admin.service.sysmgr.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.entity.sysmgr.Notice;
import com.question.admin.domain.vo.manager.sysmgr.NoticeReadVO;
import com.question.admin.mapper.sysmgr.NoticeMapper;
import com.question.admin.service.sysmgr.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public int saveReadRecord(Long id, Long userId) {
        return noticeMapper.saveReadRecord(id, userId);
    }

    @Override
    public List<User> listReadUsers(Long noticeId) {
        return noticeMapper.listReadUsers(noticeId);
    }

    @Override
    public Integer countUnread(Long userId) {
        return noticeMapper.countUnread(userId);
    }

    @Override
    public IPage<NoticeReadVO> selectPage1(Page<NoticeReadVO> page, NoticeReadVO noticeReadVO) {
        return noticeMapper.selectPage1(page, noticeReadVO);
    }
}
