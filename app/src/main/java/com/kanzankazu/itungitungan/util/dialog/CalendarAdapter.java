package com.kanzankazu.itungitungan.util.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kanzankazu.itungitungan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarAdapter extends ArrayAdapter<Date>
{
    // days with events
    private HashSet<Date> eventDays;
    // for view inflation
    private LayoutInflater inflater;
    private int selectDay;

    private boolean onChange;
    private Calendar currentDate;
    Date inOneMonth;
    private int monthPrevNext, yearPrevNext, getSelectDay;
    private String firstMonth, getSelectedMonth;

    public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays, int selectDay, String firstMonth, String getSelectedMonth)
    {
        super(context, R.layout.control_calendar_day, days);
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);

        currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MONTH, 2);
        inOneMonth = currentDate.getTime();
        this.getSelectDay = selectDay;
        this.firstMonth = firstMonth;
        this.getSelectedMonth = getSelectedMonth;
    }

    public void setEventDays(HashSet<Date> eventDays){
        this.eventDays = eventDays;
        notifyDataSetChanged();
    }

    public void validateCalendar(boolean onChange, Calendar currentDate, int selectDay, String firstMonth, String selectedMonth){
        this.getSelectDay = selectDay;
        this.onChange = onChange;
        this.currentDate = currentDate;
        this.firstMonth = firstMonth;
        this.getSelectedMonth = selectedMonth;
        monthPrevNext = currentDate.get(Calendar.MONTH);
        yearPrevNext = currentDate.get(Calendar.YEAR);
        System.out.println("year prev : "+yearPrevNext);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        Date date = getItem(position);
        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();
        System.out.println("month : "+month);
        System.out.println("year  : "+year);

        // today
        Date today = new Date();

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.control_calendar_day, parent, false);// SET CELL WITH LIST

        TextView cellView = view.findViewById(R.id.tv_cell_view);

        // if this day has an event, specify event image
        view.setBackgroundResource(0);
        if (eventDays != null)
        {
            for (Date eventDate : eventDays)
            {
                if (eventDate.getDate() == day && eventDate.getMonth() == month && eventDate.getYear() == year) {
                    // mark this day for event
                    view.setBackgroundResource(R.drawable.reminder);
                    break;
                }
            }
        }

        // clear styling
        //((TextView)view).setTypeface(null, Typeface.NORMAL);
        //((TextView)view).setTextColor(Color.BLACK);
        cellView.setTextColor(Color.BLACK);

        if (position%7 == 0){
            cellView.setTextColor(Color.RED);
        }

        try {
            selectDay = getSelectDay;
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("onchange : "+onChange);

        int selectedMonth = 0;
        try {
            selectedMonth = Integer.valueOf(getSelectedMonth);
            System.out.println("select : "+selectedMonth+"---"+month);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (date.after(inOneMonth)) {
            cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.greyed_out));
            cellView.setOnClickListener(null);
        }

        //FOR CURRENT MONTH
        if (onChange == false){
            if (year != today.getYear() || day < today.getDate() && month == today.getMonth())
            {
                //if this day is getDatebefore today
                cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.greyed_out));
                cellView.setOnClickListener(null);
            }else if (month != today.getMonth()){
                // if this day is outside current month
                if (eventDays != null){
                    cellView.setBackgroundResource(0);
                }

                System.out.println("sel in3");
                cellView.setTextColor(Color.WHITE);
                cellView.setOnClickListener(null);
            }
            else if (day == today.getDate() && month == today.getMonth()) //change today.getDate for specific day
            {
                // if it is today, set it to blue/bold

                cellView.setTypeface(null, Typeface.BOLD);
                //cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.today));
                //if selected today is today
                if (position == selectDay){
                    System.out.println("DANIEL BATUK1");
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
            }else if(position%7 == 0){
                //if this day is sunday
                System.out.println("sel in4");

                if (position == selectDay && month == today.getMonth()){
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
            }else if (position == selectDay && month == today.getMonth() && month != monthPrevNext){
                //if this day is selected day
                //((TextView)view).setTypeface(null, Typeface.BOLD);
                if (firstMonth.equals(getSelectedMonth)) {
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
            }
        //FOR NEXT/PREVIOUS MONTH
        }else {
            if (year == today.getYear() && day < today.getDate() && month == today.getMonth())
            {
                // if this day is getDatebefore today
                cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.greyed_out));
                cellView.setOnClickListener(null);
                if (month != monthPrevNext){
                    cellView.setTextColor(Color.WHITE);
                }
            }
            else if (month != monthPrevNext){
                // if this day is outside current month
                if (eventDays != null){
                    cellView.setBackgroundResource(0);
                }

                cellView.setTextColor(Color.WHITE);
                cellView.setOnClickListener(null);

            }else if (position%7 == 0){
                System.out.println("sel in1");
                //if this day is sunday
                //cellView.setTextColor(Color.RED);

                if (position == selectDay && month == selectedMonth-1 && year == today.getYear()){
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
                if (date.after(inOneMonth)) {
                    cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.greyed_out));
                    cellView.setOnClickListener(null);
                }
            }else if (day == today.getDate() && month == today.getMonth() && year == today.getYear()) {
                // if it is today, set it to blue/bold
                cellView.setTypeface(null, Typeface.BOLD);
                //cellView.setTextColor(ContextCompat.getColor(getContext(),R.color.today));
                //if selected today is today
                if (position == selectDay && month == selectedMonth-1 && year == today.getYear()){
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
            }else if (position == selectDay && month == today.getMonth() && year == today.getYear()){
                //if this day is selected day in current month
                //((TextView)view).setTypeface(null, Typeface.BOLD);
                System.out.println("sel in7");
                if (firstMonth.equals(getSelectedMonth)){
                    cellView.setTextColor(Color.WHITE);
                    cellView.setBackgroundResource(R.drawable.calendar_date_shape);
                }
            }else if (position == selectDay && month == selectedMonth-1 && year == today.getYear()){
                //if this day is selected day in next month
                System.out.println("msk sini ");
                cellView.setTextColor(Color.WHITE);
                cellView.setBackgroundResource(R.drawable.calendar_date_shape);
            }else if (position == selectDay && month == selectedMonth-1 && year != today.getYear()){
                System.out.println("msk sini year");
                //if this day is selected day in next month next year
                cellView.setTextColor(Color.WHITE);
                cellView.setBackgroundResource(R.drawable.calendar_date_shape);
            }
        }
        // set NUMBER TEXT DATE
        cellView.setText(String.valueOf(date.getDate()));

        return view;
    }
}
