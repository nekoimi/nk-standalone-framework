package com.nekoimi.standalone.framework.quartz;

import com.nekoimi.standalone.framework.utils.LocalDateTimeUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>QuartzJob</p>
 *
 * @author nekoimi 2022/4/26
 */
public abstract class QuartzJob extends QuartzJobBean {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * <p>执行Job</p>
     *
     * @param jobContext Job上下文
     */
    abstract protected void doExecute(JobExecutionContext jobContext);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("QuartzJob[{}] - START {}", getClass(), LocalDateTimeUtils.now());
            doExecute(context);
            logger.info("QuartzJob[{}] - END {}", getClass(), LocalDateTimeUtils.now());
        } catch (Exception e) {
            logger.info("QuartzJob[{}] - ERROR: {}", getClass(), e.getMessage());
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            logger.error(e.getMessage(), e);
            JobExecutionException jobE = new JobExecutionException(e);
            if (onExceptionRetry()) {
                // Quartz 会立即重新执行该任务, 不断重试，直到成功
                jobE.setRefireImmediately(true);
            } else {
                // Quartz 会自动取消所有与这个 job 有关的 trigger，从而避免再次运行 job
                jobE.setUnscheduleAllTriggers(true);
            }
            throw jobE;
        }
    }

    /**
     * <p>遇到异常是否重试</p>
     *
     * @return
     */
    protected boolean onExceptionRetry() {
        return false;
    }
}
