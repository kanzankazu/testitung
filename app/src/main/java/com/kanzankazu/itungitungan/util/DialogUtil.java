package com.kanzankazu.itungitungan.util;

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
import com.kanzankazu.itungitungan.util.Firebase.FirebaseAnalyticsUtil;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseRemoteConfigOnNext;

public class DialogUtil {

    public static void generateCustomAlertDialog(Context context, View view, String title, Boolean twoButton, @Nullable DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(title);
        if (twoButton) {
            builder.setPositiveButton(context.getText(R.string.confirm_yes), positiveListener != null ? positiveListener : (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.dismiss());
        }
        builder.setNegativeButton(context.getText(R.string.confirm_no), (dialogInterface, i) -> dialogInterface.dismiss());
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
}
