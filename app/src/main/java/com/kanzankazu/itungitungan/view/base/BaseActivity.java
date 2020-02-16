package com.kanzankazu.itungitungan.view.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.util.DialogUtil;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil;
import com.kanzankazu.itungitungan.util.SystemUtil;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.util.dialog.ProgressDialogConnection;
import com.kanzankazu.itungitungan.view.SplashActivity;
import com.kanzankazu.itungitungan.view.main.MainActivity;

public class BaseActivity extends AppCompatActivity implements BaseView, FirebaseLoginUtil.FirebaseLoginListener {

    public FirebaseLoginUtil loginUtil;
    public FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;
    ProgressDialogConnection progressDialogConnection = new ProgressDialogConnection();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        loginUtil = new FirebaseLoginUtil(this, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialogConnection.dismissProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (UserPreference.getInstance().getIsFromNotification()) {
            Utils.intentTo(this, MainActivity.class, true);
            UserPreference.getInstance().removeSharedPrefByKey(Constants.SharedPreference.FROM_NOTIFICATION);
        } else {
            super.onBackPressed();
        }
    }

    public void showRetryDialog(DialogUtil.DialogButtonListener listener) {
        DialogUtil.showRetryDialog(this, listener);
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
    public void showToast(String message, int toastLenght) {
        Utils.showToast(this, message, toastLenght);
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
    }

    @Override
    public void uiSignInSuccess(FirebaseUser firebaseUser) {
        dismissProgressDialog();
        showSnackbar(getString(R.string.message_signin_success));
        this.firebaseUser = firebaseUser;
    }

    @Override
    public void uiSignInFailed(String errorMessage) {
        dismissProgressDialog();
        showSnackbar(errorMessage);
    }

    @Override
    public void uiSignOutSuccess(Boolean isRefresh) {
        dismissProgressDialog();
        if (isRefresh) {
            showSnackbar(getString(R.string.message_signout_success));
            Utils.intentTo(this, SplashActivity.class, true);
        }
    }

    @Override
    public void uiSignOutFailed(String message) {
        dismissProgressDialog();
        showSnackbar(message);
    }

    @Override
    public void uiConnectionError(String messageError, String typeError) {
        dismissProgressDialog();
        showSnackbar(messageError + " , " + typeError);
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

    public void initBannerAds(@IdRes int id) {
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

    public void setupInterstitialAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_id_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showInterstitialAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("Lihat", "showInterstitialAd BaseActivity : " + "The interstitial wasn't loaded yet.");
        }
    }
}