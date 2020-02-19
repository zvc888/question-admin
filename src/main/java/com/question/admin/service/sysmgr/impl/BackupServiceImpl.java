package com.question.admin.service.sysmgr.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.question.admin.utils.DataBaseUtils;
import com.question.admin.config.props.DbBackupProperties;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.domain.entity.sysmgr.Backup;
import com.question.admin.domain.vo.Result;
import com.question.admin.mapper.sysmgr.BackupMapper;
import com.question.admin.service.sysmgr.BackupService;
import com.question.admin.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.Instant;
import java.util.Date;

/**
 * <p>
 * DB备份表 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2019-09-10
 */
@Service
public class BackupServiceImpl extends ServiceImpl<BackupMapper, Backup> implements BackupService {

    @Autowired
    private DbBackupProperties dbBackupProperties;

    @Override
    public Result<Backup> backup() {
        long startTime = System.currentTimeMillis();    //获取开始时间
        String relativeFile = DataBaseUtils.backup(dbBackupProperties.getInstallPath(),dbBackupProperties.getServername(),dbBackupProperties.getUsername(),dbBackupProperties.getPassword(),dbBackupProperties.getDbname(),dbBackupProperties.getBackupPath());
        long endTime = System.currentTimeMillis();
        Backup entity= new Backup();
        entity.setBackupDate(DateUtils.currentDate());
        entity.setBackupName(String.format("%s-%s-数据备份",DateUtils.getCurrentDate(),dbBackupProperties.getDbname()));
        entity.setDbName(dbBackupProperties.getDbname());
        if(!StringUtils.isEmpty(relativeFile)){
            //备份成功
            entity.setBackupPath(relativeFile);
            entity.setStatus(10);
            File file= new File(dbBackupProperties.getBackupPath()+relativeFile);
            entity.setFileSize(file.length());
        }else{
            //备份失败
            entity.setStatus(0);
        }
        entity.setRuntime((endTime - startTime)/1000);
        this.persist(entity);
        return new Result(true, "备份成功", null, Constants.TOKEN_CHECK_SUCCESS);
    }

    @Override
    public Result persist(Backup backup) {
        Date currentDate = Date.from(Instant.now());
        if (backup.getId() == null) {
            backup.setYnFlag(YNFlagStatusEnum.VALID.getCode());
            if(UserContext.getCurrentUser()!=null){
                backup.setCreator(UserContext.getCurrentUser().getAccount());
                backup.setEditor(backup.getCreator());
            }
            backup.setCreatedTime(currentDate);
            backup.setModifiedTime(currentDate);
            //新增
            baseMapper.insert(backup);
        } else {
            if(UserContext.getCurrentUser()!=null) {
                backup.setEditor(UserContext.getCurrentUser().getAccount());
            }
            backup.setModifiedTime(currentDate);
            //更新
            baseMapper.updateById(backup);
        }
        return new Result(true, "修改成功", null, Constants.TOKEN_CHECK_SUCCESS);
    }
}
