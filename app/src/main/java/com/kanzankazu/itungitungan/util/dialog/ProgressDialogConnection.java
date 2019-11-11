package com.kanzankazu.itungitungan.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.view.Window;
import android.widget.ProgressBar;

import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseAnalyticsUtil;

public class ProgressDialogConnection {

    private Dialog progressDialog;
    private ProgressBar progressBar;

    public void showProgressDialog(Activity activity, boolean isShow) {
        if (isShow) {
            try {
                if (activity != null && !activity.isFinishing()) {
                    progressDialog = new Dialog(activity);
                }
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.layout_progress_dialog);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                ProgressBar progressBar = progressDialog.findViewById(R.id.progress_bar);
                if (activity != null) {
                    progressBar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.cyan), PorterDuff.Mode.SRC_ATOP);
                }

                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //FOR ACTIVITY THAT FINISH WHEN DISMISS DIALOG
    public void dismissProgressDialog(Activity activity) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            FirebaseAnalyticsUtil.getInstance().firebaseDialogActivityEvent(activity.getClass().getSimpleName());
        }
    }
}
