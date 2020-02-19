package com.question.admin.web.wx;

import com.question.admin.utils.ModelMapperSingle;
import org.modelmapper.ModelMapper;

public class BaseWXApiController {
    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
}
