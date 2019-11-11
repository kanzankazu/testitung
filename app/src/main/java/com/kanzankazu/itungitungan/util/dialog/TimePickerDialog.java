package com.kanzankazu.itungitungan.util.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.view_interface.TimeOnClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TimePickerDialog implements OnClickListener {

    private final String TAG = TimePickerDialog.this.getClass().getSimpleName();

    private final static int TIME_PICKER_INTERVAL = 30;

    private Dialog mDateDialog;
    private Activity mActivity;

    private TimeOnClick mTimeOnClick;
    private TimePicker timePicker;
    private RadioGroup radioGroup;

    public TimePickerDialog(Activity activity) {
        mActivity = activity;
        LayoutInflater inflater = mActivity.getLayoutInflater();

        View rootView = mActivity.getLayoutInflater().inflate(R.layout.layout_time_picker, null, false);

        radioGroup = rootView.findViewById(R.id.rg_time);
        timePicker = rootView.findViewById(R.id.time_picker);
        TextView okBtn = rootView.findViewById(R.id.btn_ok);
        TextView cancelButton = rootView.findViewById(R.id.btn_cancel);

        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        String[] sChooseTime = {"Kapan Saja", "Pilih Jam"};
        for (int i = 0; i < sChooseTime.length; i++) {
            final int index = i;
            View radioButtonView = inflater.inflate(R.layout.layout_radio_button, null);
            RadioButton radioButton = radioButtonView.findViewById(R.id.custom_rb);
            radioButton.setId(i);
            radioButton.setText(sChooseTime[i]);

            if (radioButton.getParent() != null)
                ((ViewGroup) radioButton.getParent()).removeView(radioButton);
            radioGroup.addView(radioButton);
            radioGroup.check(radioGroup.getChildAt(0).getId());

            radioButton.setOnClickListener(v -> {
                if (index == 0) {
                    timePicker.setVisibility(View.GONE);
                } else {
                    timePicker.setVisibility(View.VISIBLE);
                }
            });
        }

        okBtn.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Set timer picker
        timePicker.setIs24HourView(true);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        setTimePickerInterval(timePicker);

        // Configure displayed time
        if (((minute % TIME_PICKER_INTERVAL) != 0)) {
            int minuteFloor = (minute + TIME_PICKER_INTERVAL) - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == (minuteFloor + 1) ? TIME_PICKER_INTERVAL : 0);
            if (minute >= 60) {
                minute = minute % 60;
                hour++;
            }

            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute / TIME_PICKER_INTERVAL);
        }

        // Implement dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(rootView);
        mDateDialog = builder.create();

    }

    public void setDateDialogListener(TimeOnClick listener) {
        mTimeOnClick = listener;
    }

    public void show() {
        mDateDialog.show();
    }

    /**
     * Set TimePicker interval by adding a custom minutes and hour list
     *
     * @param timePicker
     */
    private void setTimePickerInterval(TimePicker timePicker) {
        try {

            //Set available minute
            NumberPicker minutePicker = timePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));

            //Set available hour
            NumberPicker hourPicker = timePicker.findViewById(Resources.getSystem().getIdentifier("hour", "id", "android"));
            hourPicker.setMinValue(0);
            int pitAvailable = 15;
            hourPicker.setMaxValue(pitAvailable - 1);
            List<String> displayedValues2 = new ArrayList<>();
            for (int a = 7; a < 22; a++) {
                displayedValues2.add(String.format("%02d", a));
            }
            hourPicker.setDisplayedValues(displayedValues2.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_ok:

                String hour = String.format("%02d", timePicker.getCurrentHour() + 7);
                String minute = String.format("%02d", timePicker.getCurrentMinute() * 30);

                if (mTimeOnClick != null ) {

                    String dateTime;
                    if (radioGroup.getCheckedRadioButtonId() == -1) {

                    } else if (radioGroup.getCheckedRadioButtonId() == 0) {
                        dateTime = "Kapan Saja";
                        mTimeOnClick.OnTimeOnClick(dateTime);
                        mDateDialog.cancel();
                    } else {
                        dateTime = String.format("%1$s:%2$s", hour, minute);
                        mTimeOnClick.OnTimeOnClick(dateTime);
                        mDateDialog.cancel();
                    }
                }
                break;

            case R.id.btn_cancel:
                mDateDialog.cancel();
                break;

            default:
                break;
        }
    }
}
