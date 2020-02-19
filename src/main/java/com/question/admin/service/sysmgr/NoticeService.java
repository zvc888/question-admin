package com.question.admin.service.sysmgr;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.Notice;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.sysmgr.NoticeReadVO;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    int saveReadRecord(Long id, Long userId);

    List<User> listReadUsers(Long noticeId);

    Integer countUnread(Long userId);

    IPage<NoticeReadVO> selectPage1(Page<NoticeReadVO> page, NoticeReadVO noticeReadVO);
}
