package com.kanzankazu.itungitungan.view.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.view.SplashActivity;
import com.kanzankazu.itungitungan.view.util.Firebase.FirebaseAnalyticsUtil;
import com.kanzankazu.itungitungan.view.util.Firebase.FirebaseRemoteConfigOnNext;

public class DialogUtil {

    public static void generateCustomAlertDialog(Context context, View view, String title, Boolean twoButton, @Nullable DialogInterface.OnClickListener positiveListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(title);
        if (twoButton) {
            builder.setPositiveButton(context.getText(R.string.dialog_confirm_positive), positiveListener != null ? positiveListener : (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.dismiss());
        }
        builder.setNegativeButton(context.getText(R.string.dialog_confirm_negative), (dialogInterface, i) -> dialogInterface.dismiss());
    }

    public interface dialogStandartListener {
        void onClick1();
    }

    public static void dialogStandart(Activity activity, String title, String message, Boolean cancelable, dialogStandartListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //code here
                        listener.onClick1();
                    }
                })
                .show();
    }

    public static void initMaintenanceDialog(Activity mActivity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.maintenance_title));
        dialog.setPositiveButton(mActivity.getString(R.string.exit_button_title), (dialogInterface, i) -> {
            if (mActivity instanceof SplashActivity) {
                mActivity.finish();
            } else {
                mActivity.moveTaskToBack(true);
            }

            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    public static void initForceUpdateDialog(Activity mActivity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.force_update_title));

        dialog.setPositiveButton(mActivity.getString(R.string.update_button_title), (dialogInterface, i) -> {
            goToPackagePlayStore(mActivity);
            dialogInterface.dismiss();
            mActivity.finish();
        });

        dialog.setNegativeButton(mActivity.getString(R.string.exit_button_title), (dialogInterface, i) -> {
            if (mActivity instanceof SplashActivity) {
                mActivity.finish();
            } else {
                mActivity.moveTaskToBack(true);
            }

            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
        negButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    public static void initSuggestUpdateDialog(Activity mActivity, FirebaseRemoteConfigOnNext firebaseRemoteConfigurationOnNext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.suggest_update_title));

        dialog.setPositiveButton(mActivity.getString(R.string.update_button_title), (dialogInterface, i) -> {
            goToPackagePlayStore(mActivity);
            dialogInterface.dismiss();
            firebaseRemoteConfigurationOnNext.onNextAction();
        });

        dialog.setNegativeButton(mActivity.getString(R.string.continue_button_title), (dialogInterface, i) -> {
            firebaseRemoteConfigurationOnNext.onNextAction();
            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
        negButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    private static void goToPackagePlayStore(Activity mActivity) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mActivity.startActivity(myAppLinkToMarket);
            FirebaseAnalyticsUtil.getInstance().firebaseAppRateClickedEvent();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, " unable to find market app", Toast.LENGTH_LONG).show();
            FirebaseAnalyticsUtil.getInstance().firebaseAppOpenPlayStoreErrorEvent();
        }
    }

}
