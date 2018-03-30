package com.epam.health.tool.facade.common.date.util;

import java.util.Date;

/**
 * Created by Vasilina_Terehova on 3/30/2018.
 */
public class DateUtil {
    public static final int ONE_HOUR_MILLISECONDS = 3600 * 1000;

    public static Date dateHourAgo() {
        return new Date(System.currentTimeMillis() - ONE_HOUR_MILLISECONDS);
    }

    public static Date dateHourPlus(Date date) {
        return new Date(date.getTime() + ONE_HOUR_MILLISECONDS);
    }
}
