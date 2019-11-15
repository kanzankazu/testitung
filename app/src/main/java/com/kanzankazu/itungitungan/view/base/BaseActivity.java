package com.kanzankazu.itungitungan.view.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.NetworkUtil;
import com.kanzankazu.itungitungan.util.SystemUtil;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.util.dialog.ProgressDialogConnection;

public class BaseActivity extends AppCompatActivity implements BaseView {

    ProgressDialogConnection progressDialogConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialogConnection = new ProgressDialogConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialogConnection.dismissProgressDialog();
    }

    public void showRetryDialog(Utils.DialogButtonListener listener) {
        Utils.showRetryDialog(BaseActivity.this, listener);
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
        Utils.showToast(BaseActivity.this, message);
    }

    @Override
    public void showSnackbar(String message) {
        Utils.showSnackBar(BaseActivity.this, message);
    }

    @Override
    public void showSnackbar(String message, int snackbarLeght) {
        Utils.showSnackBar(BaseActivity.this, message, snackbarLeght);
    }

    @Override
    public void showSnackbar(String message, View.OnClickListener listener) {
        Utils.showSnackBar(BaseActivity.this, message, listener);
    }

    public void checkNoInternet(Utils.IntroductionButtonListener listener) {
        boolean b = new Handler().postAtTime(() -> {
            if (NetworkUtil.isConnected(BaseActivity.this)) {
                listener.onFirstButtonClick();
            } else {
                listener.onSecondButtonClick();
            }
        }, 1000);
    }

    public void hideKeyboard(View view) {
        Utils.closeSoftKeyboard(this);
    }

    public void goneView(@Nullable View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public void visibleView(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void enableView(View... views) {
        for (View view : views) {
            view.setEnabled(true);
        }
    }

    public void disableView(View... views) {
        for (View view : views) {
            view.setEnabled(false);
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