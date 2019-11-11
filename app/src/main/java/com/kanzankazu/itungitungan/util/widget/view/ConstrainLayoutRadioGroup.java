package com.kanzankazu.itungitungan.util.widget.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

public class ConstrainLayoutRadioGroup extends ConstraintLayout {
    ArrayList<RadioButton> cld = new ArrayList<>();
    int lastSelectedPos = 9000;

    public ConstrainLayoutRadioGroup(Context context) {
        super(context);
    }

    public ConstrainLayoutRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initiateViewOperation() {
        consensusView();
    }

    private void consensusView() {
        int cc = this.getChildCount();
        for (int z = 0; z < cc; z++) {
            View v = this.getChildAt(z);

            if (this.getChildAt(z) instanceof RadioButton) {
                cld.add((RadioButton) v);
                if (((RadioButton) v).isChecked()) {
                    lastSelectedPos = cld.size() - 1;
                }
            }
        }

        addLstrn();
    }

    private void addLstrn() {

        for (RadioButton b :
                cld) {
            b.setOnClickListener(v -> {
                deselectAll();
                ((RadioButton) v).setChecked(true);
            });
        }
    }

    public void deselectAll() {
        for (RadioButton w :
                cld) {
            if (w.isChecked()) {
                w.setChecked(false);
            }
        }
    }

    public void selectItem(String tag) {
        RadioButton b = (RadioButton) this.findViewWithTag(tag);
        b.setChecked(true);
    }

    public void selectItem(int pos) {
        if (pos >= 9000) {
            Log.e("", "pos is over 9000");
        } else {
            lastSelectedPos = pos;
            RadioButton b = cld.get(pos);
            b.setChecked(true);
        }
    }

    public String getSelectedTag() {
        String ret = null;
        for (RadioButton b :
                cld) {
            if (b.isChecked()) {
                ret = b.getTag().toString();
                break;
            }
        }
        return ret;
    }

    public int getSelectedIndex() {
        int ret = 9001;
        int c = 0;
        for (RadioButton b :
                cld) {
            if (b.isChecked()) {
                ret = c;
                break;
            }
            c++;
        }
        return ret;
    }
}
