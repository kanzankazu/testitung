package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.AnimatorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kanzankazu.itungitungan.R;

import java.util.List;

public class DialogUtil {

    public static void makeDialogStandart(Activity activity, String title, String message, Boolean cancelable, dialogStandartListener listener) {
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

    public static void makeDialogStandart2Button(Activity activity, String title, String message, Boolean cancelable, String textButton1, String textButton2, dialogStandart2Listener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(textButton1, (dialogInterface, i) -> {
            //code here
            listener.onClickButton1();
            dialogInterface.dismiss();
        });
        if (!TextUtils.isEmpty(textButton2)) {
            dialog.setNegativeButton(textButton2, (dialogInterface, i) -> {
                listener.onClickButton2();
                dialogInterface.dismiss();
            });
        }
        dialog.show();
    }

    public static void makeDialogStandart2Button(Activity activity, View view, String title, Boolean twoButton, @Nullable DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(title);
        if (twoButton) {
            builder.setPositiveButton(activity.getText(R.string.confirm_yes), positiveListener != null ? positiveListener : (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
        }
        builder.setNegativeButton(activity.getText(R.string.confirm_no), (dialogInterface, i) -> dialogInterface.dismiss());
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
    public static AlertDialog setupRadioAlertDialog(Activity activity, String title, CharSequence[] radioButtonData, int checkedIndex, int identifier, String mode, dialogRadioListener listener) {
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

    public static void setupCustomAlertDialog(Activity activity, @LayoutRes int layout, @AnimatorRes int anim, Boolean isCancelable, dialogCustomListener listener) {
        View dialogView = activity.getLayoutInflater().inflate(layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(isCancelable);
        alertDialog.setCanceledOnTouchOutside(isCancelable);

        if (anim != 0) {
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            }
        }

        listener.customDialogContent(dialogView, alertDialog);

        alertDialog.show();

    }

    public static CharSequence[] convertListStrinToCharSequenceArray(List<String> list) {
        return list.toArray(new CharSequence[list.size()]);
    }

    public static CharSequence[] convertToCharSequenceArray(List<CharSequence> list) {
        return list.toArray(new CharSequence[list.size()]);
    }

    public interface dialogStandartListener {
        void onClickButton();
    }

    public interface dialogStandart2Listener {
        void onClickButton1();

        void onClickButton2();
    }

    public interface dialogRadioListener {
        void onRadioButtonClick(int index, int identifier, String mode);
    }

    public interface dialogCustomListener {
        void customDialogContent(View view, AlertDialog mAlertDialog);
    }
}
