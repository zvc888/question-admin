package com.question.admin.domain.vo.manager.sysmgr;

import lombok.Data;

@Data
public class MailVO {
    private static final long serialVersionUID = 5613231124043303948L;
    private Long id;


    private Long userId;
    private String toUsers;
    private String subject;
    private String content;



}
