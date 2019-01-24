package com.mvc.cryptovault.console.util;

import java.util.Calendar;

/**
 * @author qiyichen
 * @create 2019/1/24 11:41
 */
public class TimeUtil {

    public static Long getDayZeroTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }
}
