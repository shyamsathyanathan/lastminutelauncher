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
        switch(month) {
            case Calendar.JANUARY: return "January";
            case Calendar.FEBRUARY: return "February";
            case Calendar.MARCH: return "March";
            case Calendar.APRIL: return "April";
            case Calendar.MAY: return "May";
            case Calendar.JUNE: return "June";
            case Calendar.JULY: return "July";
            case Calendar.AUGUST: return "August";
            case Calendar.SEPTEMBER: return "September";
            case Calendar.OCTOBER: return "October";
            case Calendar.NOVEMBER: return "November";
            case Calendar.DECEMBER: return "December";
            default: return "";
        }
    }
}
