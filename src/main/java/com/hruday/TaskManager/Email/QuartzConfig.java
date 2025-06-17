package com.hruday.TaskManager.Email;

import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInMinutes(1) // run every 10 min
                        .repeatForever())
                .build();
    }

    @Autowired
    private AutowiringSpringBeanJobFactory jobFactory;


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobDetail jobDetail,  Trigger trigger) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        return factory;
    }


}
