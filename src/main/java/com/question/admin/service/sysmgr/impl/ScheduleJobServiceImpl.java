package com.question.admin.service.sysmgr.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.question.admin.constant.sysmgr.ScheduleJobConsts;
import com.question.admin.domain.entity.sysmgr.ScheduleJob;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.mapper.sysmgr.ScheduleJobMapper;
import com.question.admin.scheduler.base.ScheduleAnnotation;
import com.question.admin.service.sysmgr.ScheduleJobService;
import com.question.admin.utils.AnnotationUtils;
import com.question.admin.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author zvc
 * @since 2019-08-18
 */
@Slf4j
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Override
    public boolean checkExistJob(Long id, String jobId){
        QueryWrapper<ScheduleJob> wrapper = new QueryWrapper<>();
        wrapper.eq("yn_flag", YNFlagStatusEnum.VALID.getCode());
        wrapper.eq("job_id",jobId);
        if(id!=null) {
            wrapper.ne("id", id);
        }
        List<ScheduleJob> result= baseMapper.selectList(wrapper);
        return !CollectionUtils.isEmpty(result);
    }

    @Override
    public List<ScheduleJob> findScheduleJobCombo() {
        List<ScheduleJob> vos = Lists.newArrayList();
        List<Class<?>> clsList = AnnotationUtils.getClasses(ScheduleJobConsts.JOB_PKG);
        if(CollectionUtils.isNotEmpty(clsList)) {
            clsList.stream().forEach(c -> {
                Annotation[] annotations = c.getAnnotations();
                if (annotations != null && annotations.length > 0) {
                    if (annotations[0] instanceof ScheduleAnnotation) {
                        ScheduleAnnotation annotation = (ScheduleAnnotation) annotations[0];
                        ScheduleJob vo = new ScheduleJob();
                        vo.setJobId(annotation.jobId());
                        vo.setJobName(annotation.jobName());
                        vo.setJobClass(c.getName());
                        vos.add(vo);
                    }
                }
            });
        }
        return vos;
    }

    @Override
    public void updateRuntimeJob(String jobId, Date fireTime, Date previousFireTime, Date nextFireTime, String failReason){
        QueryWrapper<ScheduleJob> wrapper = new QueryWrapper<>();
        wrapper.eq("yn_flag", YNFlagStatusEnum.VALID.getCode());
        wrapper.eq("job_id",jobId);
        List<ScheduleJob> result= baseMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(result)){
            return ;
        }
        ScheduleJob job= result.get(0);
        job.setFireTime(fireTime);
        if(previousFireTime != null) {
            job.setLastFireTime(previousFireTime);
        }
        job.setNextFireTime(nextFireTime);
        job.setFailReason(failReason);
        job.setModifiedTime(DateUtils.currentDate());
        baseMapper.updateById(job);
    }
}
