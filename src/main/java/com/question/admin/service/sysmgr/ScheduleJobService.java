package com.question.admin.service.sysmgr;

import com.question.admin.domain.QuestionServiceException;
import com.question.admin.domain.entity.sysmgr.ScheduleJob;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务 服务类
 * </p>
 *
 * @author zvc
 * @since 2019-08-18
 */
public interface ScheduleJobService extends IService<ScheduleJob> {

    /**
     * 检查 jobId 是否已经存在
     * @param id
     * @param jobId
     * @return
     * @throws QuestionServiceException
     */
    boolean checkExistJob(Long id, String jobId) throws QuestionServiceException;

    /**
     * 获取系统的Job
     *
     * @return
     */
    List<ScheduleJob> findScheduleJobCombo();

    /**
     * 更新运行时Job信息
     * @param jobId
     * @param fireTime
     * @param previousFireTime
     * @param nextFireTime
     * @param failReason
     * @throws QuestionServiceException
     */
    void updateRuntimeJob(String jobId, Date fireTime, Date previousFireTime, Date nextFireTime, String failReason) throws QuestionServiceException;

}
