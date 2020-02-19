package com.question.admin.service.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.domain.entity.exam.SwiperConfig;
import com.question.admin.mapper.exam.SwiperConfigMapper;
import com.question.admin.service.exam.SwiperConfigService;
import org.springframework.stereotype.Service;

@Service
public class SwiperConfigServiceImpl extends ServiceImpl<SwiperConfigMapper, SwiperConfig> implements SwiperConfigService {
}
