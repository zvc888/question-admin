package com.question.admin.web.admin.sysmgr;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.constant.Constants;
import com.question.admin.constant.enumtype.YNFlagStatusEnum;
import com.question.admin.domain.QuestionServiceException;
import com.question.admin.domain.entity.sysmgr.ScheduleJob;
import com.question.admin.scheduler.service.QuestionSchedulerService;
import com.question.admin.service.sysmgr.ScheduleJobService;
import com.question.admin.utils.DateUtils;
import com.question.admin.domain.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务 前端控制器
 * </p>
 *
 * @author zvc
 * @since 2019-08-18
 */
@Slf4j
@RestController
@RequestMapping("/sysmgr/schedulejob")
public class ScheduleJobController {

    @Autowired
    ScheduleJobService scheduleJobService;

    @Autowired
    private QuestionSchedulerService questionSchedulerService;

    /**
     * 分页查询
     * @param scheduleJob
     * @param pageNo
     * @param limit
     * @return
     */
    @RequiresPermissions("sysmgr.schedulejob.query")
    @RequestMapping(value="/list",method = {RequestMethod.POST,RequestMethod.GET})
    public Result list(ScheduleJob scheduleJob,
                       @RequestParam(defaultValue = "1")int pageNo,
                       @RequestParam(defaultValue = "10")int limit){
        Result result = new Result();
        Page<ScheduleJob> page = new Page(pageNo, limit);
        QueryWrapper<ScheduleJob> eWrapper = new QueryWrapper(scheduleJob);
        eWrapper.eq("yn_flag","1");
        IPage<ScheduleJob> list = scheduleJobService.page(page, eWrapper);
        result.setData(list);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 根据Id查询
     * @param scheduleJob
     * @return
     */
    @RequiresPermissions("sysmgr.schedulejob.query")
    @RequestMapping(value="/find",method = {RequestMethod.POST})
    public Result findById(@RequestBody ScheduleJob scheduleJob){
        ScheduleJob scheduleJobBean= scheduleJobService.getById(scheduleJob.getId());

        Result result = new Result();
        result.setData(scheduleJobBean);
        result.setResult(true);
        result.setCode(Constants.TOKEN_CHECK_SUCCESS);
        return result;
    }

    /**
     * 保存
     * @param scheduleJob
     * @return
     */
    @RequiresPermissions("sysmgr.schedulejob.save")
    @RequestMapping(value="/save",method = {RequestMethod.POST})
    public Result save(@RequestBody ScheduleJob scheduleJob){
        boolean startJob = scheduleJob.getStartJob() != null ? scheduleJob.getStartJob().booleanValue() : false;

        Date currentDate= DateUtils.currentDate();
        scheduleJob.setEditor(UserContext.getCurrentUser().getAccount());
        scheduleJob.setModifiedTime(currentDate);
        if(scheduleJob.getId()!=null){
            scheduleJobService.updateById(scheduleJob);

            if(startJob) {
                questionSchedulerService.updateJobCron(scheduleJob.getJobId(), scheduleJob.getCron(), scheduleJob.getStartTime());
                questionSchedulerService.resumeJob(scheduleJob.getJobId());
            } else {
                questionSchedulerService.pauseJob(scheduleJob.getJobId());
            }

        }else{
            scheduleJob.setCreator(UserContext.getCurrentUser().getAccount());
            scheduleJob.setCreatedTime(currentDate);
            scheduleJob.setYnFlag(YNFlagStatusEnum.VALID.getCode());
            scheduleJobService.save(scheduleJob);

            questionSchedulerService.addJob(scheduleJob.getJobId(), scheduleJob.getJobClass(), scheduleJob.getCron(), scheduleJob.getStartTime(), startJob);
        }
        return new Result(true,null,null, Constants.TOKEN_CHECK_SUCCESS);
    }

    /**
     * 删除
     * @param scheduleJob
     * @return
     */
    @RequiresPermissions("sysmgr.schedulejob.delete")
    @RequestMapping(value="/delete",method = {RequestMethod.POST})
    public Result dropById(@RequestBody ScheduleJob scheduleJob){
        Result result ;
        if(scheduleJob.getId()!=null){
            ScheduleJob delScheduleJob= new ScheduleJob();
            delScheduleJob.setId(scheduleJob.getId());
            delScheduleJob.setYnFlag("0");
            delScheduleJob.setEditor(UserContext.getCurrentUser().getAccount());
            delScheduleJob.setModifiedTime(Date.from(Instant.now()));
            result=new Result(scheduleJobService.updateById(delScheduleJob),null,null,Constants.TOKEN_CHECK_SUCCESS);

            questionSchedulerService.removeJob(scheduleJob.getJobId());
        }else{
            result = new Result(false, "", null ,Constants.PARAMETERS_MISSING);
        }
        return result;
    }

    /**
     * 获取下拉选项
     * @return
     */
    @RequestMapping(value="/job-options",method = {RequestMethod.POST,RequestMethod.GET})
    public Result getJobCombo() {
        List<ScheduleJob> jobList= scheduleJobService.findScheduleJobCombo();
        return new Result(true, "", jobList ,Constants.TOKEN_CHECK_SUCCESS);
    }

    @RequiresPermissions("sysmgr.schedulejob.query")
    @PostMapping("/check_job_exist")
    public boolean checkJobExist(ScheduleJob schedule) {
        boolean exist = true;
        Long id = schedule.getId();
        String jobId = schedule.getJobId();
        try {
            exist = scheduleJobService.checkExistJob(id, jobId);
        } catch (QuestionServiceException e) {
            log.error("checkJobExist schedulerJob: {} error.", schedule, e);
        }
        return exist;
    }
}
