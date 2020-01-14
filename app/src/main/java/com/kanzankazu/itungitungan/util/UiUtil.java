package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class UiUtil {

    public static void showDialog(Context context, View dialogContentView) {
        if (!(context instanceof Activity)) {
            throw new IllegalStateException("Context should be an mActivity");
        }
        Dialog dialog = new Dialog((Activity) context);
        dialog.setContentView(dialogContentView);
        dialog.show();
    }

    public static String getIDRadio(LinearLayout view) {
        return view.findViewById(((RadioGroup) ((LinearLayout) view.getChildAt(0)).getChildAt(1)).getCheckedRadioButtonId()).getTag().toString();
    }

}
