package com.question.admin.domain.vo.manager.sysmgr;

import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.domain.entity.sysmgr.Notice;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NoticeVO implements Serializable {

	private static final long serialVersionUID = 7363353918096951799L;

	private Notice notice;

	private List<User> users;
}
