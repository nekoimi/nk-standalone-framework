package com.nekoimi.standalone.framework.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;

/**
 * <p>StdQuartzJobService</p>
 *
 * @author nekoimi 2022/4/26
 */
@Slf4j
public class StdQuartzJobService implements QuartzJobService {
    private final Scheduler scheduler;

    public StdQuartzJobService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }
}
