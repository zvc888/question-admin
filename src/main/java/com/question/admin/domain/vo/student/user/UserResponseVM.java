package com.question.admin.domain.vo.student.user;

import com.question.admin.domain.entity.sysmgr.User;
import com.question.admin.utils.ModelMapperSingle;
import com.question.admin.utils.DateTimeUtil;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Data
public class UserResponseVM implements Serializable {
    private static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private Integer id;

    private String account;

    private String name;


    private String email;


    private String phone;


    private String createdTime;

    private String modifiedTime;
    private String lastPwdModifiedTime;
    private Integer status;

    public static UserResponseVM from(User user) {
        UserResponseVM vm = modelMapper.map(user, UserResponseVM.class);
        vm.setCreatedTime(DateTimeUtil.dateFormat(user.getCreatedTime()));
        vm.setModifiedTime(DateTimeUtil.dateFormat(user.getModifiedTime()));
        return vm;
    }
}
