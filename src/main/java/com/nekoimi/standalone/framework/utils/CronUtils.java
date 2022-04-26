package com.nekoimi.standalone.framework.utils;

import com.nekoimi.standalone.framework.constants.DateTimeConstants;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Nekoimi  2020/7/16 2:23
 */
public class CronUtils {

    /**
     * <p>验证表达式是否合法</p>
     *
     * @param expression
     * @return
     */
    public static boolean isValidExpression(String expression) {
        return CronExpression.isValidExpression(expression);
    }

    /**
     * <p>解析下一次时间</p>
     *
     * @param expression
     * @return
     */
    public static LocalDateTime parseNext(String expression) {
        Date now = new Date();
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(now.toInstant(), DateTimeConstants.DEFAULT_TIME_ZONE.toZoneId());
        ZonedDateTime next = CronExpression.parse(expression).next(dateTime);
        Assert.notNull(next, "expression next is null");
        return LocalDateTime.from(next.toInstant());
    }
}
