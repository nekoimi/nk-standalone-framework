package com.nekoimi.standalone.framework.config;

import com.nekoimi.standalone.framework.quartz.QuartzJobService;
import com.nekoimi.standalone.framework.quartz.StdQuartzJobService;
import com.nekoimi.standalone.framework.runner.QuartzJobRunner;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>QuartzConfiguration</p>
 *
 * @author nekoimi 2022/4/26
 */
@Slf4j
public class QuartzConfiguration {

    @Bean
    public CommandLineRunner quartzJobRunner() {
        return new QuartzJobRunner();
    }

    @Bean
    public QuartzJobService quartzJobService(Scheduler scheduler) {
        return new StdQuartzJobService(scheduler);
    }

    /**
     * <p>处理线程池</p>
     *
     * @return
     */
    @Bean(name = "quartzJobThreadPoolTaskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor quartzJobThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setQueueCapacity(1024);
        taskExecutor.setThreadNamePrefix("quartz-job-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(@Qualifier("quartzJobThreadPoolTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        return schedulerFactoryBean -> schedulerFactoryBean.setTaskExecutor(taskExecutor);
    }
}
