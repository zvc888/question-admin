package com.question.admin.web.admin.sysmgr;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.LoginUser;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.vo.manager.sysmgr.NoticeReadVO;
import com.question.admin.constant.Constants;
import com.question.admin.domain.entity.sysmgr.Notice;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.sysmgr.NoticeVO;
import com.question.admin.service.sysmgr.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "公告")
@RestController
@RequestMapping("/sysmgr/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "保存公告")
    @RequiresPermissions("sysmgr.notice.add")
    public Result saveNotice(@RequestBody Notice notice) {
        noticeService.save(notice);
        return new Result(true, null, null, Constants.TOKEN_CHECK_SUCCESS);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取公告")
    @RequiresPermissions("sysmgr.notice.query")
    public Result get(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        Result result = new Result();
        result.setData(notice);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    @GetMapping(params = "id")
    public NoticeVO readNotice(Long id) {
        NoticeVO vo = new NoticeVO();

        Notice notice = noticeService.getById(id);
        if (notice == null || notice.getStatus() == Notice.Status.DRAFT) {
            return vo;
        }
        vo.setNotice(notice);

        noticeService.saveReadRecord(notice.getId(), UserContext.getCurrentUser().getUserId());

        List<User> users = noticeService.listReadUsers(id);
        vo.setUsers(users);

        return vo;
    }

    @PutMapping
    @ApiOperation(value = "修改公告")
    @RequiresPermissions("sysmgr.notice.update")
    public Notice updateNotice(@RequestBody Notice notice) {
        Notice no = noticeService.getById(notice.getId());
        if (Notice.Status.PUBLISH == no.getStatus()) {
            throw new IllegalArgumentException("发布状态的不能修改");
        }
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.eq("id", no.getId());
        noticeService.update(notice, wrapper);
        return notice;
    }

    @GetMapping("/list")
    @ApiOperation(value = "公告管理列表")
    @RequiresPermissions("sysmgr.notice.query")
    public Result listNotice(Notice notice,
                             @RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "10") int limit) {

        Result result = new Result();
        Page<Notice> page = new Page(pageNo, limit);
        QueryWrapper<Notice> eWrapper = new QueryWrapper(notice);
//        eWrapper.eq("yn_flag","1");
        IPage<Notice> list = noticeService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除公告")
    @RequiresPermissions(value = {"sysmgr.notice.del"})
    public void delete(@PathVariable Long id) {
        noticeService.removeById(id);
    }

    @ApiOperation(value = "未读公告数")
    @GetMapping("/count-unread")
    public Integer countUnread() {
        LoginUser loginUser = UserContext.getCurrentUser();
        return noticeService.countUnread(loginUser.getUserId());
    }

    @GetMapping("/published")
    @ApiOperation(value = "公告列表")
    public Result listNoticeReadVO(NoticeReadVO noticeReadVO,
                                   @RequestParam(defaultValue = "1") int pageNo,
                                   @RequestParam(defaultValue = "10") int limit) {
        Long userId = UserContext.getCurrentUser().getUserId();
        noticeReadVO.setUserId(userId);
        Result result = new Result();
        Page<NoticeReadVO> page = new Page(pageNo, limit);
        IPage<NoticeReadVO> list = noticeService.selectPage1(page, noticeReadVO);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }
}

