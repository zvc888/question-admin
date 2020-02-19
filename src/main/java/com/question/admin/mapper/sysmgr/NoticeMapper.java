package com.question.admin.mapper.sysmgr;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.sysmgr.NoticeReadVO;
import com.question.admin.domain.entity.sysmgr.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper extends BaseMapper<Notice> {
    int saveReadRecord(@Param("noticeId") Long noticeId, @Param("userId") Long userId);

    List<User> listReadUsers(Long noticeId);

    int countUnread(Long userId);
    IPage<NoticeReadVO> selectPage1(Page<NoticeReadVO> page, @Param("notice") NoticeReadVO notice);

}
