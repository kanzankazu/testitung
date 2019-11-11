package com.kanzankazu.itungitungan.util;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final int WEEK_MILLIS = 7 * DAY_MILLIS;
    public static final int DAYS_SELECT = 1;
    public static final int HOURS_SELECT = 2;
    public static final int MINUTES_SELECT = 3;
    public static final int SECONDS_SELECT = 4;
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    public static void getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";//hh am/pm format
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
    }

    public static void getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Calendar - 24 hour format: " + formattedDate);
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static int getWeekCurrent() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    public static int getMonthCurrent() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getYearCurrent() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static String getAgeNow(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getEndDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public static String getDayFromDateString(String stringDate, SimpleDateFormat formatter) {
        //[] daysArray = new String[]{"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
        String[] daysArray = new String[]{"Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat"};
        String day = "";

        int dayOfWeek = 0;
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        //SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return day;
    }

    public static String convertStringDateToNewFormat(String timeDefault, SimpleDateFormat defaultDateFormat, SimpleDateFormat newDateFormat) {
        String mytime = timeDefault;
        //SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatDefault);
        Date myDate = null;
        try {
            myDate = defaultDateFormat.parse(mytime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //SimpleDateFormat timeFormat = new SimpleDateFormat(dateFormatNew);
        String finalDate = newDateFormat.format(myDate);
        return finalDate;
    }

    public static Date stringToDate(String dtStart, SimpleDateFormat format) {
        //String dtStart = "2010-10-15T09:27:37Z";
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }

    public static String calendarToString(Calendar cal, SimpleDateFormat dateFormat) {
        dateFormat.setTimeZone(cal.getTimeZone());
        return dateFormat.format(calendarToDate(cal));
    }

    public static Date calendarToDate(Calendar cal) {
        return cal.getTime();
    }

    public static long dateToLong(Date dates) {
        //String date = "22/3/2014";
        String date = dateToString(dates, new SimpleDateFormat("dd/MM/yyyy"));
        String parts[] = date.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }

    /**
     * @param date
     * @param year minus number would decrement the months
     * @return
     */
    public static Date addYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date
     * @param month minus number would decrement the months
     * @return
     */
    public static Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date
     * @param days minus number would decrement the days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * @param date
     * @param hour //minus number would decrement the hour
     * @return
     */
    public static Date addTimes(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour); //minus number would decrement the days
        return cal.getTime();
    }

    public static int getDayBetween2Date(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        //System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return convertLongToInt(elapsedDays);
    }

    public static int convertLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public static int getPosDateInListDate(List<Date> sourceList, Date s) {
        int position = -1;
        for (int i = 0; i < sourceList.size(); i++) {
            if (isSameDay(sourceList.get(i), s)) {
                position = i;
            }
        }
        return position;
    }

    public static List<Date> getDates(String dateString1, String dateString2, SimpleDateFormat simpleDateFormat) {
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = simpleDateFormat.parse(dateString1);
            date2 = simpleDateFormat.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getDates(date1, date2);
    }

    public static List<Date> getDates(Date date1, Date date2) {
        ArrayList<Date> dates = new ArrayList<Date>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Date getStart(Date date) {
        return clearTime(date);
    }

    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    //
    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getTimeAgo(Date date) {

        if (date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = getCurrentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {
            timeAgo = "less than a minute";
        } else if (dim == 1) {
            return "1 minute";
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " minutes";
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = "about an hour";
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = "about " + (Math.round(dim / 60)) + " hours";
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 day";
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " days";
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = "about a month";
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " months";
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = "about a year";
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = "over a year";
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = "almost 2 years";
        } else {
            timeAgo = "about " + (Math.round(dim / 525600)) + " years";
        }

        return timeAgo + " ago";
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = getCurrentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
    //

    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNowAfterDate(String my_date, SimpleDateFormat sdf) {
        try {
            Date strDate = sdf.parse(my_date);
            return isNowAfterDate(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Lihat", "isNowAfterDate DateTimeUtil : " + e.getMessage());
            return false;
        }
    }

    public static boolean isNowAfterDate(Date date) {
        if (new Date().after(date)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNowBeforeDate(String my_date, SimpleDateFormat sdf) {
        try {
            Date strDate = sdf.parse(my_date);
            return isNowBeforeDate(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Lihat", "isNowAfterDate DateTimeUtil : " + e.getMessage());
            return false;
        }
    }

    public static boolean isNowBeforeDate(Date date) {
        if (new Date().before(date)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBetween2Time(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.getDefault());
        String now = dateFormat.format(new Date());
        String qstart = start.replace(":", "");
        String qend = end.replace(":", "");
        return Integer.valueOf(now) > Integer.valueOf(qstart) && Integer.valueOf(now) < Integer.valueOf(qend);
    }

    public static boolean isBetween2Date(Date startDate, Date endDate) {
        return new Date().after(startDate) && new Date().before(endDate);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameDay(int dayOfWeek) {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        return calNow.get(Calendar.DAY_OF_WEEK) == calSet.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    public static boolean isWithinDaysPast(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysPast(cal, days);
    }

    public static boolean isWithinDaysPast(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isBeforeDay(cal, today) && !isBeforeDay(cal, future));
    }

    /**
     * @param dateString yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static String getTimeSecMinutesHoursDays(String dateString) throws ParseException {

        SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateAndTime = endDateFormat.format(new Date());
        Date endDate = endDateFormat.parse(currentDateAndTime);

        SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = startDateFormat.parse(dateString);
        // format the java.util.Date object to the desired format
        //  String startDateString = new SimpleDateFormat(Constants.DateAndMonth.SAMPLE_DATE_TIME_FORMAT).format(startDate);
        //long startMili = Tools.getMiliSecondsFromDateANDTIME(startDateString);
        //long endMili = Tools.getMiliSecondsFromDateANDTIME(currentDateAndTime);
        long difference = endDate.getTime() - startDate.getTime();

        //String[] separated = minutesHoursDays.split(":");
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);

        String minutesHoursDays = calculateTime(difference); //minutes:Hours:days

        return minutesHoursDays;

    }

    private static String calculateTime(long milis) {
        String time = "";
        int day = (int) (milis / (1000 * 60 * 60 * 24));
        int hours = (int) ((milis - (1000 * 60 * 60 * 24 * day)) / (1000 * 60 * 60));
        int minute = (int) (milis - (1000 * 60 * 60 * 24 * day) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int second = (int) ((milis / 1000) % 60);

        if (day > 0) {
            if (day == 1) {
                time = day + " day";
            } else {
                time = day + " days";
            }
        } else if (day < 1 && hours > 0) {

            if (hours == 1) {
                time = hours + " hr";
            } else {
                time = hours + " hrs";
            }
        } else if (hours < 1 && minute > 0) {
            if (minute == 1) {
                time = minute + " min";
            } else {
                time = minute + " mins";
            }
        } else if (minute < 1 && second > 0) {

            if (second == 1) {
                time = second + " sec";
            } else {
                time = second + " secs";
            }
        } else {

            if (second <= 0) {
                time = "0 sec";
            }
        }

        try {
            int timeUnitDay = (int) TimeUnit.MILLISECONDS.toDays(milis);
            long timeUnitHours = TimeUnit.MILLISECONDS.toHours(milis);
            long timeUnitMinutes = TimeUnit.MILLISECONDS.toMinutes(milis);
            long timeUnitSeconds = TimeUnit.MILLISECONDS.toSeconds(milis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  System.out.println("Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);
        //time = "Day " + timeUnitDay + " : Hour " + timeUnitHours + " : Minute " + timeUnitMinutes + " : Seconds " + timeUnitSeconds;
        time = "Day " + day + " : Hour " + hours + " : Minute " + minute + " : Seconds " + second;
        return time;
    }

    public static int getDayNow() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
