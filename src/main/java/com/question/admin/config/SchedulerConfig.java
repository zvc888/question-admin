package com.question.admin.config;

import com.question.admin.scheduler.factory.QuestionJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * 定时任务配置
 * @author zvc
 *
 */
@Configuration
public class SchedulerConfig {
	
	@Autowired
	@Qualifier("questionJobFactory")
    private QuestionJobFactory questionJobFactory;
	
	@Bean(name="questionSchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        factory.setOverwriteExistingJobs(true);
        factory.setJobFactory(questionJobFactory);
        return factory;
    }
	
	@Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactory.afterPropertiesSet(); // 必须手动触发
        return propertiesFactory.getObject();
    }
}
