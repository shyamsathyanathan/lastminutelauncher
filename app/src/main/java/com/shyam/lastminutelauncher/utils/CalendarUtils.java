package com.shyam.lastminutelauncher.utils;

import java.util.Calendar;

/**
 * Created by shyam on 27/6/15.
 */
public class CalendarUtils {

    public static String getFormattedDate() {
        Calendar cal = Calendar.getInstance();
        String date = getDay(cal.get(Calendar.DAY_OF_WEEK)) + " "
                + cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH))
                + " " + cal.get(Calendar.YEAR);
        return date;
    }

    private static String getDay(int day) {
        switch(day) {
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
            default: return "";
        }
    }

    private static String getMonth(int month) {
        return "June";
    }
}
