package com.question.admin.domain.vo.student.paper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserEventLog implements Serializable {

    private static final long serialVersionUID = -3951198127159024633L;

    public UserEventLog(Long userId, String account, String name, Date createdTime) {
        this.userId = userId;
        this.account = account;
        this.name = name;
        this.createdTime = createdTime;
    }

    private Integer id;

    private Long userId;

    private String account;

    private String name;

    private String content;

    private Date createdTime;

}
