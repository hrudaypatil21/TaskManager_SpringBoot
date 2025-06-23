package com.hruday.TaskManager.Email;

import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
public class QuartzConfig {


    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(TaskReminderJob.class)
                .withIdentity("taskReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("taskReminderTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(5, 0))
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobDetail jobDetail,  Trigger trigger, AutowiringSpringBeanJobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.scheduler.instanceName", "TaskReminderScheduler");
        quartzProperties.setProperty("org.quartz.threadPool.threadCount", "5");

        schedulerFactory.setJobFactory(jobFactory);
        schedulerFactory.setJobDetails(jobDetail);
        schedulerFactory.setTriggers(trigger);
        schedulerFactory.setQuartzProperties(quartzProperties);
        schedulerFactory.setAutoStartup(true);

        return schedulerFactory;
    }

}
