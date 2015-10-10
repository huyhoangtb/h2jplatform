/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class DateTimeUtils {

    public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String dd_MM_yyyy = "dd-MM-yyyy";
    public static final String HHmm = "HH:mm";

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(format);
        return mySimpleDateFormat.format(date);
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(ddMMyyyy);
        return mySimpleDateFormat.format(date);
    }

    public static int daysBetween(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Date convertDateToDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(ddMMyyyy);

        try {
            return mySimpleDateFormat.parse(mySimpleDateFormat.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date copyTime(Date fromDate, Date toDate) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(fromDate);   // assigns calendar to given date 
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = calendar.get(Calendar.MINUTE);        // gets phut
        toDate = convertDateToDate(toDate);

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(toDate); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hour); // adds one hour
        cal.add(Calendar.MINUTE, minute); // adds one hour
        return cal.getTime();
    }

    public static String toParam(Date date) {
        return convertDateToString(date, dd_MM_yyyy);
    }

    public static Date asParam(String date) {
        return convertStringToDate(date, dd_MM_yyyy);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date convertStringToDate(String dateStr, String format) {
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(format);
        try {
            return mySimpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat(ddMMyyyy);
        try {
            return mySimpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Lay thu tiep theo ke tu ngay hien tai
     *
     * @param startDate
     * @param dayOfWeek
     * @return
     */
    public static Date getNextDayOfWeek(int dayOfWeek) {

        Calendar c = Calendar.getInstance();
        Date today = new Date();
        c.setTime(new Date());
        int day = c.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_WEEK) + dayOfWeek;
        c.set(Calendar.DAY_OF_MONTH, day);
        if (c.getTime().before(today)) {
            c.setTime(addDays(c.getTime(), 7));
        }
        return c.getTime();
//        while (true) {
//           
//            if (c.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
//                return c.getTime();
//            }
//           c.setTime(addDays(c.getTime(), 1));
//        }

    }

    public static List<Date> getDayOfWeek(Date startDate, Date toDate, DayOfWeek dayOfWeek) {

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        int day = c.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_WEEK) + dayOfWeek.toInteger();
        c.set(Calendar.DAY_OF_MONTH, day);
        if (c.getTime().before(startDate)) {
            c.setTime(addDays(c.getTime(), 7));
        }
        if (c.getTime().after(toDate)) {
            return null;
        }
        List<Date> resultList = new ArrayList<Date>();
        resultList.add(convertStringToDate(convertDateToString(c.getTime())));
        while (true) {
            c.setTime(addDays(c.getTime(), 7));
            if (c.getTime().after(toDate)) {
                break;
            }
            resultList.add(convertStringToDate(convertDateToString(c.getTime())));
        }

        return resultList;
    }

    public static String getDayNameShortInWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "CN";
            case Calendar.MONDAY:
                return "T2";
            case Calendar.TUESDAY:
                return "T3";
            case Calendar.WEDNESDAY:
                return "T4";
            case Calendar.THURSDAY:
                return "T5";
            case Calendar.FRIDAY:
                return "T6";
            case Calendar.SATURDAY:
                return "T7";
        }
        return "";
    }

    public static String getDayNameInWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Chủ nhuật";
            case Calendar.MONDAY:
                return "Thứ 2";
            case Calendar.TUESDAY:
                return "Thứ 3";
            case Calendar.WEDNESDAY:
                return "Thứ 4";
            case Calendar.THURSDAY:
                return "Thứ 5";
            case Calendar.FRIDAY:
                return "Thứ 6";
            case Calendar.SATURDAY:
                return "Thứ 7";
        }
        return "";
    }

    public static int getDayInWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static Boolean checkDayBetween(Date checkDate, Date fromDate, Date toDate) {
        if (!checkDate.before(fromDate) && !checkDate.after(toDate)) {
            return true;
        }
        return false;
    }

    public static Integer getYear(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getCurrentYear() {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static void main(String[] ar) {
        Date date = new Date();
        Date day = convertDateToDate(date);
        System.out.println("date: " + date);
        ;
        System.out.println(copyTime(date, day));

    }
}
