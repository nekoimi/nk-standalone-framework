package com.nekoimi.standalone.framework.quartz;

import org.quartz.JobExecutionContext;

/**
 * <p>HelloWorldJob</p>
 *
 * @author nekoimi 2022/4/26
 */
public class HelloWorldJob extends QuartzJob {

    @Override
    protected void doExecute(JobExecutionContext jobContext) {
        logger.info(">>>>>>>>>>>> Hello World, {}", System.currentTimeMillis());
    }
}
