package com.question.admin.service.sysmgr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.question.admin.domain.entity.sysmgr.Backup;
import com.question.admin.domain.vo.Result;

/**
 * <p>
 * DB备份表 服务类
 * </p>
 *
 * @author zvc
 * @since 2019-09-10
 */
public interface BackupService extends IService<Backup> {

    /**
     * 备份
     */
    Result<Backup> backup();

    /**
     * 保存
     * @param backup
     * @return
     */
    Result persist(Backup backup);
}
