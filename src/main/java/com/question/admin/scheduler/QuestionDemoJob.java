package com.question.admin.scheduler;

import com.question.admin.scheduler.base.ScheduleAnnotation;
import com.question.admin.scheduler.base.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

@Slf4j
@ScheduleAnnotation(jobId = "QuestionDemoJob", jobName = "框架演示Job")
public class QuestionDemoJob extends BaseJob {

	@Override
	public void execute(JobExecutionContext context){
		log.info("QUESTION-ADMIN： 你好！");
	}

}
