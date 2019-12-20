package com.kanzankazu.itungitungan.util;

import android.widget.TextView;

import com.kanzankazu.itungitungan.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

    private static int DAYS_IN_WEEK = 7;
    private static String dateFromCalendar;

    public static void parseDate(Calendar calendar, TextView textView, String inputDate) {
        if (inputDate != null) {
            try {
                Date date = dateFormatter.parse(inputDate);
                calendar.setTime(date);

                String sDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String sMonth = setMonthToLocaleINAComplete(date);
                String sYear = String.valueOf(calendar.get(Calendar.YEAR));
                textView.setText(sDate + " " + sMonth + " " + sYear);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setInDateFormalFormat(Calendar calendar, TextView textView) {
        String sDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String sMonth = setMonthToLocaleINAComplete(calendar.getTime());
        String sYear = String.valueOf(calendar.get(Calendar.YEAR));
        textView.setText(sDate + " " + sMonth + " " + sYear);
    }

    public static String setInDateFormalFormatFromString(String inputDate) {
        if (inputDate != null && !inputDate.isEmpty()) {
            Date date = convertStringToDate(inputDate);
            Calendar calendar = Calendar.getInstance();
            System.out.println("Date: " + inputDate + " " + date);
            calendar.setTime(date);

            String sDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String sMonth = setMonthToLocalIndon(calendar.getTime());
            String sYear = String.valueOf(calendar.get(Calendar.YEAR));

            return sDate + " " + sMonth + " " + sYear;
        } else {
            return "";
        }
    }

    public static String setInDateFormalFormatFromStringIncompleteMonth(String inputDate) {
        if (inputDate != null && !inputDate.isEmpty()) {
            Date date = convertStringToDate(inputDate);
            Calendar calendar = Calendar.getInstance();
            System.out.println("Date: " + inputDate + " " + date);
            calendar.setTime(date);

            String sDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String sMonth = setMonthToLocaleINAIncomplete(calendar.getTime());
            String sYear = String.valueOf(calendar.get(Calendar.YEAR));

            return sDate + " " + sMonth + " " + sYear;
        } else {
            return "";
        }
    }

    public static String setDateSlashFormatFromString(String inputDate) {
        Date date = convertStringToDate(inputDate);
        Calendar calendar = Calendar.getInstance();
        System.out.println("Date: " + inputDate + " " + date);
        calendar.setTime(date);

        String sDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        int sMonth = calendar.get(Calendar.MONTH);
        String sYear = String.valueOf(calendar.get(Calendar.YEAR));

        return sDate + "/" + (sMonth + 1) + "/" + sYear;
    }

    public static String setMonthToLocaleINAIncomplete(Date mDate) {
        SimpleDateFormat halfMonthFormat = new SimpleDateFormat("MMM");
        String halfMonth = halfMonthFormat.format(mDate);

        return halfMonth;
    }

    public static String setMonthToLocaleINAComplete(Date mDate) {
        SimpleDateFormat completeMonthFormat = new SimpleDateFormat("MMMM");
        String completeMonth = completeMonthFormat.format(mDate);

        return completeMonth;
    }

    public static String setMonthToLocalIndon(Date mDate) {
        SimpleDateFormat completeMonthFormat = new SimpleDateFormat("MMMM");
        String completeMonth = completeMonthFormat.format(mDate);
        String completeMonthIndo = "";

        if (completeMonth.equals("January")) {
            completeMonthIndo = "Januari";
        } else if (completeMonth.equals("February")) {
            completeMonthIndo = "Februari";
        } else if (completeMonth.equals("March")) {
            completeMonthIndo = "Maret";
        } else if (completeMonth.equals("April")) {
            completeMonthIndo = "April";
        } else if (completeMonth.equals("May")) {
            completeMonthIndo = "Mei";
        } else if (completeMonth.equals("June")) {
            completeMonthIndo = "Juni";
        } else if (completeMonth.equals("July")) {
            completeMonthIndo = "Juli";
        } else if (completeMonth.equals("August")) {
            completeMonthIndo = "Agustus";
        } else if (completeMonth.equals("September")) {
            completeMonthIndo = "September";
        } else if (completeMonth.equals("October")) {
            completeMonthIndo = "Oktober";
        } else if (completeMonth.equals("November")) {
            completeMonthIndo = "November";
        } else if (completeMonth.equals("December")) {
            completeMonthIndo = "Desember";
        }

        return completeMonthIndo;
    }

    public static String setDayToLocaleINAIncomplete(Date mDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        String day = dateFormat.format(mDate);
        String halfCompleteDay = "";

        if (day.equals("Mon")) {
            halfCompleteDay = "Mon";
        } else if (day.equals("Tue")) {
            halfCompleteDay = "Tue";
        } else if (day.equals("Wed")) {
            halfCompleteDay = "Wed";
        } else if (day.equals("Thu")) {
            halfCompleteDay = "Thu";
        } else if (day.equals("Fri")) {
            halfCompleteDay = "Fri";
        } else if (day.equals("Sat")) {
            halfCompleteDay = "Sat";
        } else if (day.equals("Sun")) {
            halfCompleteDay = "Sun";
        }

        return halfCompleteDay;
    }

    public static String setDayToLocaleINAComplete(Date mDate) {
        SimpleDateFormat completeDateFormat = new SimpleDateFormat("EEEE");
        String completeDay = completeDateFormat.format(mDate);

        return completeDay;
    }

    public static String setDayToLocaleINACompleteFromString(String bookingDate) {
        Date date = DateTimeUtils.convertStringToDate(bookingDate);
        SimpleDateFormat completeDateFormat = new SimpleDateFormat("EEEE");
        String completeDay = completeDateFormat.format(date);

        return completeDay;
    }

    public static String getDateIncomplete(Date mDate) {
        SimpleDateFormat halfDayFormat = new SimpleDateFormat("d");
        String halfDay = halfDayFormat.format(mDate);

        return halfDay;
    }

    public static String getDateComplete(Date mDate) {
        SimpleDateFormat completeDayFormat = new SimpleDateFormat("dd");
        String completeDay = completeDayFormat.format(mDate);

        return completeDay;
    }

    public static String getYear(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String year = simpleDateFormat.format(date);

        return year;
    }

    public static String getTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String time = simpleDateFormat.format(date);

        return time;
    }

    public static Date convertStringToDate(String dateString) {
        Date dateTime = null;
        try {
            dateTime = dateFormatter.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String convertDateToString(Date dateTime) {
        String dateString = null;
        try {
            dateString = dateFormatter.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static List<Date> getListForBookingDate(Date mDate) {
        List<Date> bookingDateList = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            calendar.add(Calendar.DAY_OF_WEEK, i);
            if (calendar.getTime().before(getDateInOneMonth())) {
                bookingDateList.add(calendar.getTime());
            } else {
                break;
            }
        }

        return bookingDateList;
    }

    public static Date getDateInOneMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        return calendar.getTime();
    }

    public static String getDateFromCalendar() {
        return dateFromCalendar;
    }

    public static void setDateFromCalendar(String dateFromCalendar) {
        DateTimeUtils.dateFromCalendar = dateFromCalendar;
    }

    public static String setTimeToPit(String time) {
        System.out.println("DATETIME INPUT SET TIME TO PIT : " + time);
        //INPUT 07:00 -> 7.0
        //INPUT 13:00 -> 13.0
        //INPUT 16:30 -> OUTPUT 16.5
        String[] pit = time.split(":");

        String pitNow = "";

        if (pit[0].startsWith("0")) {
            pitNow = pit[0].substring(1, 2);
        } else {
            pitNow = pit[0];
        }

        if (pit[1].equals("30")) {
            pitNow = pitNow + ".5";
        } else {
            pitNow = pitNow + ".0";
        }

        System.out.println("DATETIME OUTPUT SET TIME TO PIT : " + pitNow);
        return pitNow;
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String date = convertDateToString(calendar.getTime());
        return date;
    }

    public static String convertTime(String time) {
        Date dateTime = new Date();
        String time2;
        try {
            dateTime = timeFormatter.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        time2 = timeFormatter.format(dateTime);

        return time2;
    }

    public static String convertTimeFromString(String inputDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String timePattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);

        Date date = null;
        String sTime = null;

        try {
            date = inputFormat.parse(inputDate);
            sTime = timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sTime;
    }
}
