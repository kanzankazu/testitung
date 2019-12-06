package com.kanzankazu.itungitungan.view.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;
import com.kanzankazu.itungitungan.util.NetworkUtil;
import com.kanzankazu.itungitungan.util.SystemUtil;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.util.dialog.ProgressDialogConnection;
import com.kanzankazu.itungitungan.view.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity implements BaseView {

    ProgressDialogConnection progressDialogConnection;
    private Snackbar snackConnect;

    public FirebaseDatabaseUtil databaseUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseUtil = new FirebaseDatabaseUtil();

        progressDialogConnection = new ProgressDialogConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialogConnection.dismissProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (UserPreference.getInstance().getIsFromNotification()) {
            Utils.intentWithClearTask(this, MainActivity.class);
        } else {
            super.onBackPressed();
        }
    }

    public void showRetryDialog(Utils.DialogButtonListener listener) {
        Utils.showRetryDialog(this, listener);
    }

    @Override
    public void showProgressDialog() {
        progressDialogConnection.showProgressDialog(this, true);
    }

    @Override
    public void dismissProgressDialog() {
        progressDialogConnection.dismissProgressDialog();
    }

    @Override
    public void showToast(String message) {
        Utils.showToast(this, message);
    }

    @Override
    public void showSnackbar(String message) {
        Utils.showSnackBar(this, message);
    }

    @Override
    public void showSnackbar(String message, int snackbarLeght) {
        Utils.showSnackBar(this, message, snackbarLeght);
    }

    @Override
    public void showSnackbar(String message, View.OnClickListener listener) {
        Utils.showSnackBar(this, message, listener);
    }

    @Override
    public void showSnackbarNoConnection() {
        snackConnect = Snackbar.make(findViewById(android.R.id.content), getString(R.string.message_no_internet_network), Snackbar.LENGTH_INDEFINITE);

        View view = snackConnect.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

        /*// calculate actionbar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        // set margin
        params.setMargins(0, actionBarHeight, 0, 0);*/

        view.setLayoutParams(params);
    }

    private void showSnackbarNoConnectionToggle(Boolean isShow) {
        if (isShow) {
            snackConnect.show();
        } else {
            snackConnect.dismiss();
        }
    }

    public void isConnectInet() {

    }

    public void hideKeyboard() {
        Utils.closeSoftKeyboard(this);
    }

    public void visibleView(Boolean isVisible, View... views) {
        for (View view : views) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void enableView(Boolean isEnable, View... views) {
        for (View view : views) {
            view.setEnabled(isEnable);
        }
    }

    public void initAds(@IdRes int id) {
        AdView mAdView = findViewById(id);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                SystemUtil.visibileAnim(getBaseContext(), mAdView, View.VISIBLE, R.anim.masuk_dari_bawah);
            }
        });
    }


}