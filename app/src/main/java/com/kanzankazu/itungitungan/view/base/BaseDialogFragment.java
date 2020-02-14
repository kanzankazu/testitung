package com.kanzankazu.itungitungan.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.DialogUtil;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil;
import com.kanzankazu.itungitungan.util.SystemUtil;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.util.dialog.ProgressDialogConnection;
import com.kanzankazu.itungitungan.view.SplashActivity;

public class BaseDialogFragment extends DialogFragment implements BaseView, FirebaseLoginUtil.FirebaseLoginListener {

    public FirebaseLoginUtil loginUtil;
    public FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;
    public AppCompatActivity mActivity;
    ProgressDialogConnection progressDialogConnection = new ProgressDialogConnection();
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        loginUtil = new FirebaseLoginUtil(mActivity, this);

    }

    @Override
    public void onResume() {
        if (mRewardedVideoAd != null) mRewardedVideoAd.resume(mActivity);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mRewardedVideoAd != null) mRewardedVideoAd.pause(mActivity);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        progressDialogConnection.dismissProgressDialog();
        if (mRewardedVideoAd != null) mRewardedVideoAd.destroy(mActivity);
        super.onDestroy();
    }

    @Override
    public void showRetryDialog(DialogUtil.DialogButtonListener listener) {
        DialogUtil.showRetryDialog(mActivity, listener);
    }

    @Override
    public void showProgressDialog() {
        progressDialogConnection.showProgressDialog(mActivity, true);
    }

    @Override
    public void dismissProgressDialog() {
        progressDialogConnection.dismissProgressDialog();
    }

    @Override
    public void showToast(String message) {
        Utils.showToast(mActivity, message);
    }

    @Override
    public void showSnackbar(String message) {
        Utils.showSnackBar(mActivity, message);
    }

    @Override
    public void showSnackbar(String message, int snackbarLenght) {
        Utils.showSnackBar(mActivity, message, snackbarLenght);
    }

    @Override
    public void showSnackbar(String message, View.OnClickListener listener) {
        Utils.showSnackBar(mActivity, message, listener);
    }

    @Override
    public void showSnackbarNoConnection() {

    }

    /*FLU*/
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
            Utils.intentTo(mActivity, SplashActivity.class, true);
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
        Utils.closeSoftKeyboard(mActivity);
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
        AdView mAdView = mActivity.findViewById(id);
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
                SystemUtil.visibileAnim(mActivity, mAdView, View.VISIBLE, R.anim.masuk_dari_bawah);
            }
        });
    }

    public void setupInterstitialAds() {
        mInterstitialAd = new InterstitialAd(mActivity);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_id_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void showInterstitialAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("Lihat", "showInterstitialAd Base : " + "The interstitial wasn't loaded yet.");
        }
    }

    public void setupRewardVideoLegacyApi(RewardedVideoAdListener listener) {
        MobileAds.initialize(mActivity, getString(R.string.ads_id_app));
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mRewardedVideoAd.setRewardedVideoAdListener(listener);

        loadRewardedVideoAd();
    }

    public void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(mActivity.getString(R.string.ads_id_reward_video), new AdRequest.Builder().build());
    }

    public void showRewardedAds() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            Log.d("Lihat", "showRewardedAds Base : " + "The reward video wasn't loaded yet.");
        }
    }
}
