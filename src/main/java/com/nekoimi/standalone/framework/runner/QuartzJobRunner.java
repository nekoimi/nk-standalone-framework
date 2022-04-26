package com.nekoimi.standalone.framework.runner;

import com.nekoimi.standalone.framework.holder.ContextHolder;
import com.nekoimi.standalone.framework.quartz.HelloWorldJob;
import org.quartz.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

/**
 * <p>QuartzJobRunner</p>
 *
 * @author nekoimi 2022/4/26
 */
public class QuartzJobRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        ApplicationContext context = ContextHolder.getInstance();
        Scheduler scheduler = context.getBean(Scheduler.class);
        // Hello World
        JobDetail jobDetail = JobBuilder.newJob(HelloWorldJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("helloworld1", "group1")
                .startNow() // 立即生效
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? *"))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.startDelayed(10);
    }
}
