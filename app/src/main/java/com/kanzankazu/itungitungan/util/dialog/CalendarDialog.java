package com.kanzankazu.itungitungan.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.widget.DateTimeUtils;
import com.kanzankazu.itungitungan.view_interface.CalendarOnClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarDialog extends Dialog {
    private ImageView btnPrev,btnNext,btnDismiss;
    private TextView txtDate;
    private GridView grid;
    private static final int DAYS_COUNT = 42;
    private int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season association
    private int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};
    private CalendarAdapter calAdapter;
    private HashSet<Date> events;
    private Calendar currentDate = Calendar.getInstance();
    private String currMonthYear = "", newCurrMonthYear = "", currMonth = "", currYear = "", currDay = "", currStartDay = "";
    private String firstMonth= "", firstYear = "", selectedMonth = "", selectedYear = "";
    private CalendarOnClick mDateOnClick;
    private boolean isFirstMonth = true;
    private int selectDay;

    public String getFirstMonth() {return firstMonth;}

    public void setFirstMonth(String firstMonth) {this.firstMonth = firstMonth;}

    public int getSelectDay() {return selectDay;}

    public void setSelectDay(int selectDay) {this.selectDay = selectDay;}

    public String getFirstYear() {return firstYear;}

    public void setFirstYear(String firstYear) {this.firstYear = firstYear;}

    public String getSelectedYear() {return selectedYear;}

    public void setSelectedYear(String selectedYear) {this.selectedYear = selectedYear;}

    public String getSelectedMonth() {return selectedMonth;}

    public void setSelectedMonth(String selectedMonth) {this.selectedMonth = selectedMonth;}

    public CalendarDialog(Context context) {super(context);}

    public CalendarDialog(Context context, int themeResId) {super(context, themeResId);}

    public void setDateClick(CalendarOnClick mDateOnClick) {this.mDateOnClick = mDateOnClick;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_calendar);

        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        btnDismiss = findViewById(R.id.iv_calendar_dismiss);
        txtDate = findViewById(R.id.calendar_date_display);
        grid = findViewById(R.id.calendar_grid);

        updateCalendar();
        assignClickHandlers();

        // add event
       /* Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 14) ;
        Date nowDate = cal.getTime();

        cal.add(Calendar.DATE, 8) ;
        Date nowDate1 = cal.getTime();

        events = new HashSet<>();
        events.add(nowDate);
        events.add(nowDate1);

        System.out.println("hash set2 : "+events);
        calAdapter.setEventDays(events);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void assignClickHandlers()
    {

        int addedMonth,firstMonth = 0,selectMonth = 0,addedYear,firstYear,selectYear;

        if (getSelectedMonth() == null){
            System.out.println("msk tes false");
        }else {
            if (!getSelectedMonth().equals(getFirstMonth())){
                try {
                    firstMonth = Integer.valueOf(getFirstMonth());
                    selectMonth = Integer.valueOf(getSelectedMonth());

                    firstYear = Integer.valueOf(getFirstYear());
                    selectYear = Integer.valueOf(getSelectedYear());

                    addedMonth = selectMonth - firstMonth;
                    addedYear = selectYear - firstYear;
                    System.out.println("added month & year : "+addedMonth+"=="+addedYear);
                    System.out.println("added month & select month & first month  : "+addedMonth+"=="+selectMonth+"=="+firstMonth);

                    currentDate.add(Calendar.MONTH, addedMonth);
                    currentDate.add(Calendar.YEAR, addedYear);
                    updateCalendar(events);
                    validateCalendar();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (firstMonth+2 == selectMonth){ // why + 2 because 2 month from now
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                }else {
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }else {
                btnPrev.setVisibility(View.INVISIBLE);
            }
        }

        // add one month and refresh UI
        btnNext.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1); // next button +1 month
            updateCalendar(events);
            validateCalendar();
            int currMonth = currentDate.get(Calendar.MONTH)+1;
            validateNextPrevButton(currMonth);
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1); //prev button -1 month
            updateCalendar(events);
            validateCalendar();
            int currMonth = currentDate.get(Calendar.MONTH)+1;
            validateNextPrevButton(currMonth);
        });

        btnDismiss.setOnClickListener(view -> dismiss());

        // long-pressing a day
        grid.setOnItemLongClickListener((adapterView, view, position, id) -> {
            String currDate = (String) ((TextView) view).getText();
            getCurrentDay(position);
            Toast.makeText(getContext(), ""+currStartDay+" "+currDate+" "+currMonthYear, Toast.LENGTH_SHORT).show();
            dismiss();
            return false;
        });

        grid.setOnItemClickListener((adapterView, view, position, l) -> {
            try {
                String currDate = (String) ((TextView) view).getText();
                if (currDate.length() == 1) {
                    currDate = "0" + currDate;
                }

                getCurrentDay(position);
                String dateFromCalendar = newCurrMonthYear + "-" + currDate;
                DateTimeUtils.setDateFromCalendar(dateFromCalendar);

                mDateOnClick.onCalendarOnClick(dateFromCalendar, position, getFirstMonth(), getFirstYear(), currMonth, currYear);

                int currentDay, selectedDay;
                currentDay = Integer.valueOf(currDay) - 2;
                selectedDay = Integer.valueOf(currDate) - 3;
                System.out.println("curr and select : " + currentDay + "==" + selectedDay);

                dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    public void updateCalendar() {
        updateCalendar(null);
    }

    public void updateCalendar(HashSet<Date> events)
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        System.out.println("Before - "+calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("After - "+calendar.getTime());
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        System.out.println("After2 - "+calendar.getTime());

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            //FORMAT CELL BASED ON CALENDAR CALCULATION
            cells.add(calendar.getTime());
            //ISI 1 PER 1 BARIS
            calendar.add(Calendar.DAY_OF_MONTH, 1); // BELAKANGNYA DIGANTI n MENJADI KELIPATAN n
        }

        // update grid
        calAdapter = new CalendarAdapter(getContext(), cells, events, getSelectDay(), getFirstMonth(), getSelectedMonth());
        grid.setAdapter(calAdapter);

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat daySdf = new SimpleDateFormat("dd");
        SimpleDateFormat monthSdf = new SimpleDateFormat("MM");
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
        txtDate.setText(sdf.format(currentDate.getTime()));
        currMonthYear = sdf.format(currentDate.getTime());
        newCurrMonthYear = newSdf.format(currentDate.getTime());
        currYear = yearSdf.format(currentDate.getTime());
        currDay = daySdf.format(currentDate.getTime());

        currMonth = monthSdf.format(currentDate.getTime());      //for validate month
        if (isFirstMonth){
            setFirstMonth(currMonth);
            setFirstYear(currYear);

            //Disable prev button on first show
            btnPrev.setVisibility(View.INVISIBLE);
            isFirstMonth = false;
        }

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        //header.setBackgroundColor(ContextCompat.getColor(getContext(),color));
    }

    public void validateCalendar() {
        boolean onChange = true;
        calAdapter.validateCalendar(onChange,currentDate, getSelectDay(), getFirstMonth(), getSelectedMonth());
    }

    private void validateNextPrevButton(int currMonth) {
        if (!getFirstMonth().equals("")){
            int firstMonth = Integer.valueOf(getFirstMonth());
            int limitMonth = firstMonth+2;
            System.out.println("curr month : "+currMonth+"=="+firstMonth);
            if (firstMonth == currMonth){
                btnNext.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.INVISIBLE);
            }else if (currMonth == limitMonth){
                btnNext.setVisibility(View.INVISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
            }else {
                btnNext.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void getCurrentDay(int position){
        //GET DAY IN CURRENT POSITION
        int result = position%7;
        switch (result){
            case 0:
                currStartDay = "Sun";
                break;
            case 1:
                currStartDay = "Mon";
                break;
            case 2:
                currStartDay = "Tues";
                break;
            case 3:
                currStartDay = "Wed";
                break;
            case 4:
                currStartDay = "Thurs";
                break;
            case 5:
                currStartDay = "Fri";
                break;
            case 6:
                currStartDay = "Sat";
                break;
        }
    }

}
