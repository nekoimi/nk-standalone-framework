package com.nekoimi.standalone.framework.constants;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * nekoimi  2021/12/6 11:44
 * <p>时间相关
 */
public interface DateTimeConstants {
    String SHANGHAI_ZONE = "Asia/Shanghai";
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.CHINA);
    DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA);
    TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
}
