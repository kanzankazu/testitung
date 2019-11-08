package com.kanzankazu.itungitungan.view.util.widget.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioGroup;

public class ToggleRadioButton extends AppCompatRadioButton {

    private String tagid;

    public ToggleRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void toggle() {
        if (isChecked()) {
            if (getParent() instanceof RadioGroup) {
                ((RadioGroup) getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }

    @Override
    public Object getTag() {
        if (tagid != null) {
            return tagid;
        } else {
            return super.getTag();
        }
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        if (tag instanceof String) {
            tagid = (String) tag;
        }
    }

}
