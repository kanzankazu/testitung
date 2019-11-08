package com.kanzankazu.itungitungan.view.util.widget.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxReactiveFrameLayout extends FrameLayout {

    private List<String> childList = new ArrayList<>();

    public CheckBoxReactiveFrameLayout(Context context) {
        super(context);
    }

    public CheckBoxReactiveFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBoxReactiveFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CheckBoxReactiveFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @NonNull
    private static String removeLast(String str) {
        return str.substring(0, str.length() - 1);
    }

    public void addChildRefID(String refID) {
        childList.add(refID);
    }

    public List<String> getChildRefID() {
        return childList;
    }

    //use if child is checkboxes
    public String getSelectedCheckbox() {
        String returnString = "";
        for (String id :
                childList) {
            CheckBox beta = (CheckBox) this.findViewWithTag(id);
            if (beta.isChecked()) {
                returnString += beta.getTag().toString() + ",";
            }
        }
        if (returnString != "") {
            returnString = removeLast(returnString);
        }
        return returnString;
    }

    public Integer[] getSelectedCheckboxIndex() {
        List<Integer> returnArray = new ArrayList<>();
        int tot = 0;
        for (String id :
                childList) {
            CheckBox beta = (CheckBox) this.findViewWithTag(id);
            if (beta.isChecked()) {
                returnArray.add(tot);
            }
            tot++;
        }
        return returnArray.toArray(new Integer[returnArray.size()]);
    }

}