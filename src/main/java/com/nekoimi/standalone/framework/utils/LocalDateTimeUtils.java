package com.nekoimi.standalone.framework.utils;

import com.nekoimi.standalone.framework.constants.DateTimeConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>LocalDateTimeUtils</p>
 *
 * @author nekoimi 2022/4/26
 */
public class LocalDateTimeUtils {

    /**
     * <p>获取当前时间</p>
     *
     * @return
     */
    public static String now() {
        return nowFormat(DateTimeConstants.DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * <p>格式化当前日期时间</p>
     *
     * @param formatter 格式化规则
     * @return
     */
    public static String nowFormat(String formatter) {
        return format(LocalDateTime.now(), formatter);
    }

    /**
     * <p>格式化日期时间</p>
     *
     * @param dateTime  日期时间
     * @param formatter 格式化规则
     * @return
     */
    public static String format(LocalDateTime dateTime, String formatter) {
        return DateTimeFormatter.ofPattern(formatter).format(dateTime);
    }

    /**
     * <p>格式化日期</p>
     *
     * @param dateTime  日期
     * @param formatter 格式化规则
     * @return
     */
    public static String format(LocalDate dateTime, String formatter) {
        return DateTimeFormatter.ofPattern(formatter).format(dateTime);
    }
}
