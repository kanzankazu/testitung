package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kanzankazu.itungitungan.R;

import java.util.List;

public class DialogUtil {

    public static void generateCustomAlertDialog(Context context, View view, String title, Boolean twoButton, @Nullable DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(title);
        if (twoButton) {
            builder.setPositiveButton(context.getText(R.string.confirm_yes), positiveListener != null ? positiveListener : (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
        }
        builder.setNegativeButton(context.getText(R.string.confirm_no), (dialogInterface, i) -> dialogInterface.dismiss());
    }

    public static void dialogStandart(Activity activity, String title, String message, Boolean cancelable, dialogStandartListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton("OK", (dialogInterface, i) -> {
            //code here
            listener.onClickButton();
            dialogInterface.dismiss();
        });
        dialog.show();
    }

    /*
    *
    *
    custom_alert_dialog
    *
    <?xml version="1.0" encoding="utf-8"?>
    <CheckedTextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@android:id/text1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:minHeight="?android:attr/listPreferredItemHeight"
    android:textAppearance="?android:attr/textAppearanceListItemSmall"
    android:textColor="?android:attr/textColorAlertDialogListItem"
    style="@style/TextLabelStyle"
    android:gravity="center_vertical"
    android:paddingLeft="@dimen/padding_margin_16dp"
    android:paddingRight="@dimen/padding_margin_16dp"
    android:checkMark="@null"
    android:drawableLeft="?android:attr/listChoiceIndicatorSingle"
    android:drawableRight="@null"
    android:ellipsize="marquee" />
    * */
    private AlertDialog setupRadioAlertDialog(Activity activity, String title, CharSequence[] radioButtonData, int checkedIndex, int identifier, String mode, DialogRadioListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        TextView titleTv = new TextView(activity);
        titleTv.setTypeface(ResourcesCompat.getFont(activity, R.font.avenir_next_ltpro_demi));
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        titleTv.setTypeface(Typeface.DEFAULT_BOLD);
        titleTv.setTextColor(activity.getResources().getColor(R.color.navy_blue));
        titleTv.setText(title);
        titleTv.setPadding(48, 48, 48, 24);

        dialogBuilder.setCustomTitle(titleTv);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(activity, R.layout.custom_alert_dialog, radioButtonData);

        dialogBuilder.setSingleChoiceItems(adapter, checkedIndex, (dialogInterface, index) -> {
            Utils.closeSoftKeyboard(activity);
            listener.onRadioButtonClick(index, identifier, mode);
            dialogInterface.dismiss();
        });
        return dialogBuilder.create();
    }

    public static CharSequence[] convertListStrinToCharSequenceArray(List<String> list){
        return list.toArray(new CharSequence[list.size()]);
    }

    public static CharSequence[] convertToCharSequenceArray(List<CharSequence> list) {
        return list.toArray(new CharSequence[list.size()]);
    }

    public interface dialogStandartListener {
        void onClickButton();
    }

    public interface DialogRadioListener {
        void onRadioButtonClick(int index, int identifier, String mode);
    }
}
