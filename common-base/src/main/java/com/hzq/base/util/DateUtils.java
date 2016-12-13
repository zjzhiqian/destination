package com.hzq.base.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hzq on 16/12/13.
 */
public class DateUtils {
    public static boolean isSameDayWithToday(Date date) {
        if (date == null) {
            return false;
        }
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();

        todayCal.setTime(new Date());
        dateCal.setTime(date);
        int subYear = todayCal.get(Calendar.YEAR) - dateCal.get(Calendar.YEAR);
        int subMouth = todayCal.get(Calendar.MONTH) - dateCal.get(Calendar.MONTH);
        int subDay = todayCal.get(Calendar.DAY_OF_MONTH) - dateCal.get(Calendar.DAY_OF_MONTH);
        if (subYear == 0 && subMouth == 0 && subDay == 0) {
            return true;
        }
        return false;
    }
}
