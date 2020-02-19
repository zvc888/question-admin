package com.question.admin.service.baseinfo;

import com.question.admin.domain.entity.baseinfo.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author zvc
 * @since 2019-07-12
 */
public interface DictService extends IService<Dict> {

    /**
     * 批量保存
     * @param dictList
     * @return
     */
    Boolean batchSave(List<Dict> dictList);
}
