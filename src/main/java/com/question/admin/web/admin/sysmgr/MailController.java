package com.question.admin.web.admin.sysmgr;

import com.question.admin.domain.vo.manager.sysmgr.MailVO;
import com.question.admin.service.common.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "邮件")
@RestController
@RequestMapping("/sysmgr/mail")
public class MailController {

	@Autowired
	private MailService mailService;

	@RequestMapping(value="/",method = {RequestMethod.POST})
	@ApiOperation(value = "保存邮件")
	@RequiresPermissions("mail:send")
	public MailVO save(@RequestBody MailVO mail) {
		String toUsers = mail.getToUsers().trim();
		if (StringUtils.isEmpty(toUsers)) {
			throw new IllegalArgumentException("收件人不能为空");
		}

		toUsers = toUsers.replace(" ", "");
		toUsers = toUsers.replace("；", ";");
		String[] strings = toUsers.split(";");

		List<String> toUser = Arrays.asList(strings);
		toUser = toUser.stream().filter(u -> !StringUtils.isBlank(u)).map(u -> u.trim()).collect(Collectors.toList());
//		mailService.persist(mail, toUser);

		return mail;
	}
//
//	@GetMapping("/{id}")
//	@ApiOperation(value = "根据id获取邮件")
//	@RequiresPermissions("mail:all:query")
//	public MailVO get(@PathVariable Long id) {
//		return mailService.getById(id);
//	}
//
//	@GetMapping("/{id}/to")
//	@ApiOperation(value = "根据id获取邮件发送详情")
//	@RequiresPermissions("mail:all:query")
//	public List<MailTo> getMailTo(@PathVariable Long id) {
//		return mailDao.getToUsers(id);
//	}
//
//	@GetMapping
//	@ApiOperation(value = "邮件列表")
//	@RequiresPermissions("mail:all:query")
//	public PageTableResponse list(PageTableRequest request) {
//		return new PageTableHandler(new CountHandler() {
//
//			@Override
//			public int count(PageTableRequest request) {
//				return mailDao.count(request.getParams());
//			}
//		},new ListHandler() {
//
//			@Override
//			public List<MailVO> list(PageTableRequest request) {
//				return mailDao.list(request.getParams(), request.getOffset(), request.getLimit());
//			}
//		}).handle(request);
//	}

}
