package com.question.admin.domain.vo.manager.exam;

import com.question.admin.utils.ModelMapperSingle;
import lombok.Data;
import org.modelmapper.ModelMapper;


@Data
public class BaseVM {
    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();


}
