package com.kanzankazu.itungitungan.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
        super.onResume();
    }

    @Override
    public void onDestroy() {
        progressDialogConnection.dismissProgressDialog();
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
        Utils.showToast(getActivity(), message);
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

    public void initAds(@IdRes int id) {
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
}
