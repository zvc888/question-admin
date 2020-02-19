package com.question.admin.service.baseinfo.impl;

import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.baseinfo.Dict;
import com.question.admin.mapper.baseinfo.DictMapper;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.service.baseinfo.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2019-07-12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Transactional
    public Boolean batchSave(List<Dict> dictList) {
        for(Dict dict: dictList){
            if(dict.getId()!=null){
                this.updateById(dict);
            }else{
                dict.setYnFlag(YNFlagStatusEnum.VALID.getCode());
                dict.setCreatedTime(Date.from(Instant.now()));
                dict.setEditor(UserContext.getCurrentUser().getAccount());
                dict.setCreator(UserContext.getCurrentUser().getAccount());
                this.save(dict);
            }
        }
        return true;
    }
}
