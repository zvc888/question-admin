package com.question.admin.domain.vo.student.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserUpdateVM {

    @NotBlank
    private String name;

    private String phone;
    @Email
    private String email;

}
