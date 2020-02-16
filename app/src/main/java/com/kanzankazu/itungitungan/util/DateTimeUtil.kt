package com.kanzankazu.itungitungan.util

import android.util.Log
import android.widget.TextView
import com.kanzankazu.itungitungan.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object DateTimeUtil {
    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS
    var WEEK_MILLIS = 7 * DAY_MILLIS
    val DAYS_SELECT = 1
    val HOURS_SELECT = 2
    val MINUTES_SELECT = 3
    val SECONDS_SELECT = 4
    var MAX_DATE = Date(java.lang.Long.MAX_VALUE)
    var dateFormatter = SimpleDateFormat(Constants.DATE_FORMAT_DAY_MONTH, Locale.US)
    var timeFormatter = SimpleDateFormat("HH:mm", Locale.US)
    private val DAYS_IN_WEEK = 7
    var dateFromCalendar: String? = null

    val dayNow: Int
        get() {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_WEEK)
        }

    val dateInOneMonth: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 1)

            return calendar.time
        }

    /**Get Start*/

    val currentDate: Date = Calendar.getInstance().time

    val currentDateString: String?
        get() {
            val calendar = Calendar.getInstance()
            return convertDateToString(calendar.time)
        }

    fun currentTimeUsingDate() {
        val date = Date()
        val strDateFormat = "hh:mm:ss a"//hh am/pm format
        val dateFormat = SimpleDateFormat(strDateFormat)
        val formattedDate = dateFormat.format(date)
        println("Current time of the day using Date - 12 hour format: $formattedDate")
    }

    fun currentTimeUsingCalendar() {
        val cal = Calendar.getInstance()
        val date = cal.time
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val formattedDate = dateFormat.format(date)
        println("Current time of the day using Calendar - 24 hour format: $formattedDate")
    }

    val currentWeek: Int get() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)

    val currentMonth: Int get() = Calendar.getInstance().get(Calendar.MONTH)

    val currentYear: Int get() = Calendar.getInstance().get(Calendar.YEAR)

    fun getAgeNow(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val ageInt = age

        return ageInt.toString()
    }

    fun getFirstDateOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return cal.time
    }

    fun getEndDateOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.DATE, cal.getMaximum(Calendar.DATE))
        return cal.time
    }

    fun getDayFromDateString(stringDate: String, formatter: SimpleDateFormat): String {
        //[] daysArray = new String[]{"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
        val daysArray = arrayOf("Sabtu", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat")
        var day = ""

        var dayOfWeek = 0
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        //SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        val date: Date
        try {
            date = formatter.parse(stringDate)
            val c = Calendar.getInstance()
            c.time = date
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1
            if (dayOfWeek < 0) {
                dayOfWeek += 7
            }
            day = daysArray[dayOfWeek]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return day
    }

    /**Convert Start*/

    fun convertStringDateToNewFormat(timeDefault: String, defaultDateFormat: SimpleDateFormat, newDateFormat: SimpleDateFormat): String {
//SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatDefault);
        var myDate: Date? = null
        try {
            myDate = defaultDateFormat.parse(timeDefault)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        //SimpleDateFormat timeFormat = new SimpleDateFormat(dateFormatNew);
        return newDateFormat.format(myDate)
    }

    fun convertStringToDate(dtStart: String, format: SimpleDateFormat): Date? {
        //String dtStart = "2010-10-15T09:27:37Z";
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return format.parse(dtStart)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun convertDateToString(date: Date, dateFormat: SimpleDateFormat): String {
        return dateFormat.format(date)
    }

    fun convertCalendarToString(cal: Calendar, dateFormat: SimpleDateFormat): String {
        dateFormat.timeZone = cal.timeZone
        return dateFormat.format(convertCalendarToDate(cal))
    }

    fun convertCalendarToDate(cal: Calendar): Date {
        return cal.time
    }

    fun convertDateToLong(dates: Date): Long {
        //String date = "22/3/2014";
        val date = convertDateToString(dates, SimpleDateFormat("dd/MM/yyyy"))
        val parts = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val day = Integer.parseInt(parts[0])
        val month = Integer.parseInt(parts[1])
        val year = Integer.parseInt(parts[2])

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        return calendar.timeInMillis
    }

    fun convertStringToDate(dateString: String): Date? {
        var dateTime: Date? = null
        try {
            dateTime = dateFormatter.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dateTime
    }

    fun convertDateToString(dateTime: Date): String? {
        var dateString: String? = null
        try {
            dateString = dateFormatter.format(dateTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dateString
    }

    fun convertTimeToString(time: String): String {
        var dateTime = Date()
        val time2: String
        try {
            dateTime = timeFormatter.parse(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        time2 = timeFormatter.format(dateTime)

        return time2
    }

    fun convertStringToTime(inputDate: String): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val timePattern = "HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern)
        val timeFormat = SimpleDateFormat(timePattern)

        var date: Date? = null
        var sTime: String? = null

        try {
            date = inputFormat.parse(inputDate)
            sTime = timeFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return sTime
    }

    fun convertLongToInt(l: Long): Int {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw IllegalArgumentException("$l cannot be cast to int without changing its value.")
        }
        return l.toInt()
    }

    /**Is Start*/
    fun isNowAfterDate(my_date: String, sdf: SimpleDateFormat): Boolean {
        return try {
            val strDate = sdf.parse(my_date)
            isNowAfterDate(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.d("Lihat", "isNowAfterDate DateTimeUtil : " + e.message)
            false
        }

    }

    fun isNowAfterDate(date: Date): Boolean {
        return Date().after(date)
    }

    fun isNowBeforeDate(my_date: String, sdf: SimpleDateFormat): Boolean {
        return try {
            val strDate = sdf.parse(my_date)
            isNowBeforeDate(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.d("Lihat", "isNowAfterDate DateTimeUtil : " + e.message)
            false
        }

    }

    fun isNowBeforeDate(date: Date): Boolean {
        return Date().before(date)
    }

    fun isBetween2Time(start: String, end: String): Boolean {
        val dateFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        val now = dateFormat.format(Date())
        val qstart = start.replace(":", "")
        val qend = end.replace(":", "")
        return Integer.valueOf(now) > Integer.valueOf(qstart) && Integer.valueOf(now) < Integer.valueOf(qend)
    }

    fun isBetween2Date(startDate: Date, endDate: Date): Boolean {
        return Date().after(startDate) && Date().before(endDate)
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameDay(dayOfWeek: Int): Boolean {
        val calNow = Calendar.getInstance()
        val calSet = calNow.clone() as Calendar

        calSet.set(Calendar.DAY_OF_WEEK, dayOfWeek)

        return calNow.get(Calendar.DAY_OF_WEEK) == calSet.get(Calendar.DAY_OF_WEEK)
    }

    fun isToday(date: Date): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    fun isToday(cal: Calendar): Boolean {
        return isSameDay(cal, Calendar.getInstance())
    }

    fun isBeforeDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isBeforeDay(cal1, cal2)
    }

    fun isBeforeDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) false else cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isAfterDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isAfterDay(cal1, cal2)
    }

    fun isAfterDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) true else cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isWithinDaysFuture(date: Date?, days: Int): Boolean {
        if (date == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val cal = Calendar.getInstance()
        cal.time = date
        return isWithinDaysFuture(cal, days)
    }

    fun isWithinDaysFuture(cal: Calendar?, days: Int): Boolean {
        if (cal == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val today = Calendar.getInstance()
        val future = Calendar.getInstance()
        future.add(Calendar.DAY_OF_YEAR, days)
        return isAfterDay(cal, today) && !isAfterDay(cal, future)
    }

    fun isWithinDaysPast(date: Date?, days: Int): Boolean {
        if (date == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val cal = Calendar.getInstance()
        cal.time = date
        return isWithinDaysPast(cal, days)
    }

    fun isWithinDaysPast(cal: Calendar?, days: Int): Boolean {
        if (cal == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val today = Calendar.getInstance()
        val future = Calendar.getInstance()
        future.add(Calendar.DAY_OF_YEAR, days)
        return isBeforeDay(cal, today) && !isBeforeDay(cal, future)
    }

    /**Other Start*/

    /**
     * @param date
     * @param year minus number would decrement the months
     * @return
     */
    fun addYear(date: Date, year: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, year) //minus number would decrement the days
        return cal.time
    }

    /**
     * @param date
     * @param month minus number would decrement the months
     * @return
     */
    fun addMonth(date: Date, month: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, month) //minus number would decrement the days
        return cal.time
    }

    /**
     * @param date
     * @param days minus number would decrement the days
     * @return
     */
    fun addDays(date: Date, days: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, days) //minus number would decrement the days
        return cal.time
    }

    /**
     * @param date
     * @param hour //minus number would decrement the hour
     * @return
     */
    fun addTimes(date: Date, hour: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.HOUR, hour) //minus number would decrement the days
        return cal.time
    }

    fun getDayBetween2Date(startDate: Date, endDate: Date): Int {
        //milliseconds
        var different = endDate.time - startDate.time

        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        //System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return convertLongToInt(elapsedDays)
    }

    fun getPosDateInListDate(sourceList: List<Date>, s: Date): Int {
        var position = -1
        for (i in sourceList.indices) {
            if (isSameDay(sourceList[i], s)) {
                position = i
            }
        }
        return position
    }

    fun getDates(dateString1: String, dateString2: String, simpleDateFormat: SimpleDateFormat): List<Date> {
        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = simpleDateFormat.parse(dateString1)
            date2 = simpleDateFormat.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return getDates(date1, date2)
    }

    fun getDates(date1: Date?, date2: Date?): List<Date> {
        val dates = ArrayList<Date>()

        val cal1 = Calendar.getInstance()
        cal1.time = date1

        val cal2 = Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun getStart(date: Date): Date? {
        return getStartTime(date)
    }

    fun getStartTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    fun getEndTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.time
    }

    fun getDateafter(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.after(d2)) d1 else d2
    }

    fun getDatebefore(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.before(d2)) d1 else d2
    }

    fun getTimeAgoByTimeStamp(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        // TODO: localize
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

    fun getTimeAgoByDate(date: Date?): String? {

        if (date == null) {
            return null
        }

        val time = date.time

        val curDate = currentDate
        val now = curDate.time
        if (time > now || time <= 0) {
            return null
        }

        val dim = getTimeDistanceInMinutes(time)

        var timeAgo: String

        if (dim == 0) {
            timeAgo = "less than a minute"
        } else if (dim == 1) {
            return "1 minute"
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = "$dim minutes"
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = "about an hour"
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = "about " + Math.round((dim / 60).toFloat()) + " hours"
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 day"
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = Math.round((dim / 1440).toFloat()).toString() + " days"
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = "about a month"
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = Math.round((dim / 43200).toFloat()).toString() + " months"
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = "about a year"
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = "over a year"
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = "almost 2 years"
        } else {
            timeAgo = "about " + Math.round((dim / 525600).toFloat()) + " years"
        }

        return "$timeAgo ago"
    }

    fun getTimeDistanceInMinutes(time: Long): Int {
        val timeDistance = currentDate.time - time
        return Math.round((abs(timeDistance) / 1000 / 60).toFloat())
    }

    fun hasTime(date: Date?): Boolean {
        if (date == null) {
            return false
        }
        val c = Calendar.getInstance()
        c.time = date
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true
        }
        return if (c.get(Calendar.SECOND) > 0) {
            true
        } else c.get(Calendar.MILLISECOND) > 0
    }

    /**
     * @param dateString yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun getTimeSecMinutesHoursDays(dateString: String): String {

        val endDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDateAndTime = endDateFormat.format(Date())
        val endDate = endDateFormat.parse(currentDateAndTime)

        val startDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startDate = startDateFormat.parse(dateString)
        // format the java.util.Date object to the desired format
        //  String startDateString = new SimpleDateFormat(Constants.DateAndMonth.SAMPLE_DATE_TIME_FORMAT).format(startDate);
        //long startMili = Tools.getMiliSecondsFromDateANDTIME(startDateString);
        //long endMili = Tools.getMiliSecondsFromDateANDTIME(currentDateAndTime);
        val difference = endDate.time - startDate.time

        //String[] separated = minutesHoursDays.split(":");
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);

        return calculateTime(difference)

    }

    private fun calculateTime(milis: Long): String {
        var time = ""
        val day = (milis / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((milis - 1000 * 60 * 60 * 24 * day) / (1000 * 60 * 60)).toInt()
        val minute = (milis - (1000 * 60 * 60 * 24 * day).toLong() - (1000 * 60 * 60 * hours).toLong()).toInt() / (1000 * 60)
        val second = (milis / 1000 % 60).toInt()

        if (day > 0) {
            if (day == 1) {
                time = "$day day"
            } else {
                time = "$day days"
            }
        } else if (day < 1 && hours > 0) {

            if (hours == 1) {
                time = "$hours hr"
            } else {
                time = "$hours hrs"
            }
        } else if (hours < 1 && minute > 0) {
            if (minute == 1) {
                time = "$minute min"
            } else {
                time = "$minute mins"
            }
        } else if (minute < 1 && second > 0) {

            if (second == 1) {
                time = "$second sec"
            } else {
                time = "$second secs"
            }
        } else {

            if (second <= 0) {
                time = "0 sec"
            }
        }

        try {
            val timeUnitDay = TimeUnit.MILLISECONDS.toDays(milis).toInt()
            val timeUnitHours = TimeUnit.MILLISECONDS.toHours(milis)
            val timeUnitMinutes = TimeUnit.MILLISECONDS.toMinutes(milis)
            val timeUnitSeconds = TimeUnit.MILLISECONDS.toSeconds(milis)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //  System.out.println("Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);
        //time = "Day " + timeUnitDay + " : Hour " + timeUnitHours + " : Minute " + timeUnitMinutes + " : Seconds " + timeUnitSeconds;
        time = "Day $day : Hour $hours : Minute $minute : Seconds $second"
        return time
    }

    /**/
    fun parseDate(calendar: Calendar, textView: TextView, inputDate: String?) {
        if (inputDate != null) {
            try {
                val date = dateFormatter.parse(inputDate)
                calendar.time = date

                val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
                val sMonth = setMonthToLocaleINAComplete(date)
                val sYear = calendar.get(Calendar.YEAR).toString()
                textView.text = "$sDate $sMonth $sYear"

            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
    }

    fun setInDateFormalFormat(calendar: Calendar, textView: TextView) {
        val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val sMonth = setMonthToLocaleINAComplete(calendar.time)
        val sYear = calendar.get(Calendar.YEAR).toString()
        textView.text = "$sDate $sMonth $sYear"
    }

    fun setInDateFormalFormatFromString(inputDate: String?): String {
        if (inputDate != null && !inputDate.isEmpty()) {
            val date = convertStringToDate(inputDate)
            val calendar = Calendar.getInstance()
            println("Date: $inputDate $date")
            calendar.time = date

            val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val sMonth = setMonthToLocalIndon(calendar.time)
            val sYear = calendar.get(Calendar.YEAR).toString()

            return "$sDate $sMonth $sYear"
        } else {
            return ""
        }
    }

    fun setInDateFormalFormatFromStringIncompleteMonth(inputDate: String?): String {
        if (inputDate != null && !inputDate.isEmpty()) {
            val date = convertStringToDate(inputDate)
            val calendar = Calendar.getInstance()
            println("Date: $inputDate $date")
            calendar.time = date

            val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val sMonth = setMonthToLocaleINAIncomplete(calendar.time)
            val sYear = calendar.get(Calendar.YEAR).toString()

            return "$sDate $sMonth $sYear"
        } else {
            return ""
        }
    }

    fun setDateSlashFormatFromString(inputDate: String): String {
        val date = convertStringToDate(inputDate)
        val calendar = Calendar.getInstance()
        println("Date: $inputDate $date")
        calendar.time = date

        val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val sMonth = calendar.get(Calendar.MONTH)
        val sYear = calendar.get(Calendar.YEAR).toString()

        return sDate + "/" + (sMonth + 1) + "/" + sYear
    }

    fun setMonthToLocaleINAIncomplete(mDate: Date): String {
        val halfMonthFormat = SimpleDateFormat("MMM")

        return halfMonthFormat.format(mDate)
    }

    fun setMonthToLocaleINAComplete(mDate: Date): String {
        val completeMonthFormat = SimpleDateFormat("MMMM")

        return completeMonthFormat.format(mDate)
    }

    fun setMonthToLocalIndon(mDate: Date): String {
        val completeMonthFormat = SimpleDateFormat("MMMM")
        val completeMonth = completeMonthFormat.format(mDate)
        var completeMonthIndo = ""

        if (completeMonth == "January") {
            completeMonthIndo = "Januari"
        } else if (completeMonth == "February") {
            completeMonthIndo = "Februari"
        } else if (completeMonth == "March") {
            completeMonthIndo = "Maret"
        } else if (completeMonth == "April") {
            completeMonthIndo = "April"
        } else if (completeMonth == "May") {
            completeMonthIndo = "Mei"
        } else if (completeMonth == "June") {
            completeMonthIndo = "Juni"
        } else if (completeMonth == "July") {
            completeMonthIndo = "Juli"
        } else if (completeMonth == "August") {
            completeMonthIndo = "Agustus"
        } else if (completeMonth == "September") {
            completeMonthIndo = "September"
        } else if (completeMonth == "October") {
            completeMonthIndo = "Oktober"
        } else if (completeMonth == "November") {
            completeMonthIndo = "November"
        } else if (completeMonth == "December") {
            completeMonthIndo = "Desember"
        }

        return completeMonthIndo
    }

    fun setDayToLocaleINAIncomplete(mDate: Date): String {
        val dateFormat = SimpleDateFormat("EEE")
        val day = dateFormat.format(mDate)
        var halfCompleteDay = ""

        if (day == "Mon") {
            halfCompleteDay = "Mon"
        } else if (day == "Tue") {
            halfCompleteDay = "Tue"
        } else if (day == "Wed") {
            halfCompleteDay = "Wed"
        } else if (day == "Thu") {
            halfCompleteDay = "Thu"
        } else if (day == "Fri") {
            halfCompleteDay = "Fri"
        } else if (day == "Sat") {
            halfCompleteDay = "Sat"
        } else if (day == "Sun") {
            halfCompleteDay = "Sun"
        }

        return halfCompleteDay
    }

    fun setDayToLocaleINAComplete(mDate: Date): String {
        val completeDateFormat = SimpleDateFormat("EEEE")

        return completeDateFormat.format(mDate)
    }

    fun setDayToLocaleINACompleteFromString(bookingDate: String): String {
        val date = DateTimeUtil.convertStringToDate(bookingDate)
        val completeDateFormat = SimpleDateFormat("EEEE")

        return completeDateFormat.format(date)
    }

    fun getDateIncomplete(mDate: Date): String {
        val halfDayFormat = SimpleDateFormat("d")

        return halfDayFormat.format(mDate)
    }

    fun getDateComplete(mDate: Date): String {
        val completeDayFormat = SimpleDateFormat("dd")

        return completeDayFormat.format(mDate)
    }

    fun getYear(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy")

        return simpleDateFormat.format(date)
    }

    fun getTime(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("hh:mm")

        return simpleDateFormat.format(date)
    }

    fun getListForBookingDate(mDate: Date): List<Date> {
        val bookingDateList = ArrayList<Date>()
        for (i in 0 until DAYS_IN_WEEK) {
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            calendar.add(Calendar.DAY_OF_WEEK, i)
            if (calendar.time.before(dateInOneMonth)) {
                bookingDateList.add(calendar.time)
            } else {
                break
            }
        }

        return bookingDateList
    }

    fun setTimeToPit(time: String): String {
        println("DATETIME INPUT SET TIME TO PIT : $time")
        //INPUT 07:00 -> 7.0
        //INPUT 13:00 -> 13.0
        //INPUT 16:30 -> OUTPUT 16.5
        val pit = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var pitNow = ""

        if (pit[0].startsWith("0")) {
            pitNow = pit[0].substring(1, 2)
        } else {
            pitNow = pit[0]
        }

        if (pit[1] == "30") {
            pitNow = "$pitNow.5"
        } else {
            pitNow = "$pitNow.0"
        }

        println("DATETIME OUTPUT SET TIME TO PIT : $pitNow")
        return pitNow
    }
}
