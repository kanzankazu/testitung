package id.otomoto.otr.utils

import android.annotation.SuppressLint
import android.widget.TextView
import com.kanzankazu.itungitungan.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Stefanus Candra on 25/11/2019.
 */
object DateTimeUtils {

    var dateFormatter = SimpleDateFormat(Constants.DATE_TIME_FORMAT_DAY_MONTH, Locale.US)
    var timeFormatter = SimpleDateFormat("HH:mm", Locale.US)

    private val DAYS_IN_WEEK = 7
    private var dateFromCalendar: String = ""

    @SuppressLint("SetTextI18n")
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
        return if (inputDate != null && !inputDate.isEmpty()) {
            val date = convertStringToDate(inputDate)
            val calendar = Calendar.getInstance()
            println("Date: $inputDate $date")
            calendar.time = date

            val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val sMonth = setMonthToLocalIndon(calendar.time)
            val sYear = calendar.get(Calendar.YEAR).toString()

            "$sDate $sMonth $sYear"
        } else {
            ""
        }
    }

    fun setInDateFormalFormatFromStringIncompleteMonth(inputDate: String?): String {
        return if (inputDate != null && !inputDate.isEmpty()) {
            val date = convertStringToDate(inputDate)
            val calendar = Calendar.getInstance()
            println("Date: $inputDate $date")
            calendar.time = date

            val sDate = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val sMonth = setMonthToLocaleINAIncomplete(calendar.time)
            val sYear = calendar.get(Calendar.YEAR).toString()

            "$sDate $sMonth $sYear"
        } else {
            ""
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


    @SuppressLint("SimpleDateFormat")
    fun setMonthToLocaleINAIncomplete(mDate: Date): String {
        val halfMonthFormat = SimpleDateFormat("MMM")
        return halfMonthFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun setMonthToLocaleINAComplete(mDate: Date): String {
        val completeMonthFormat = SimpleDateFormat("MMMM")
        return completeMonthFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun setMonthToLocalIndon(mDate: Date): String {
        val completeMonthFormat = SimpleDateFormat("MMMM")
        val completeMonth = completeMonthFormat.format(mDate)
        var completeMonthIndo = ""

        when (completeMonth) {
            "January" -> completeMonthIndo = "Januari"
            "February" -> completeMonthIndo = "Februari"
            "March" -> completeMonthIndo = "Maret"
            "April" -> completeMonthIndo = "April"
            "May" -> completeMonthIndo = "Mei"
            "June" -> completeMonthIndo = "Juni"
            "July" -> completeMonthIndo = "Juli"
            "August" -> completeMonthIndo = "Agustus"
            "September" -> completeMonthIndo = "September"
            "October" -> completeMonthIndo = "Oktober"
            "November" -> completeMonthIndo = "November"
            "December" -> completeMonthIndo = "Desember"
        }
        return completeMonthIndo
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayToLocaleINAIncomplete(mDate: Date): String {
        val dateFormat = SimpleDateFormat("EEE")
        val day = dateFormat.format(mDate)
        var halfCompleteDay = ""

        when (day) {
            "Mon" -> halfCompleteDay = "Mon"
            "Tue" -> halfCompleteDay = "Tue"
            "Wed" -> halfCompleteDay = "Wed"
            "Thu" -> halfCompleteDay = "Thu"
            "Fri" -> halfCompleteDay = "Fri"
            "Sat" -> halfCompleteDay = "Sat"
            "Sun" -> halfCompleteDay = "Sun"
        }
        return halfCompleteDay
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayToLocaleINAComplete(mDate: Date): String {
        val completeDateFormat = SimpleDateFormat("EEEE")
        return completeDateFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayToLocaleINACompleteFromString(bookingDate: String): String {
        val date = convertStringToDate(bookingDate)
        val completeDateFormat = SimpleDateFormat("EEEE")
        return completeDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateIncomplete(mDate: Date): String {
        val halfDayFormat = SimpleDateFormat("d")
        return halfDayFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateComplete(mDate: Date): String {
        val completeDayFormat = SimpleDateFormat("dd")
        return completeDayFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun getYear(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        return simpleDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("hh:mm")
        return simpleDateFormat.format(date)
    }

    fun getCurrentDate(): String? {
        val calendar = Calendar.getInstance()
        return convertDateToString(calendar.time)
    }

    fun convertTime(time: String): String {
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

    fun convertTimeFromString(inputDate: String): String? {
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

    fun getDateInOneMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    fun getDateFromCalendar(): String {
        return this.dateFromCalendar
    }

    fun setDateFromCalendar(dateFromCalendar: String) {
        this.dateFromCalendar = dateFromCalendar
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

}