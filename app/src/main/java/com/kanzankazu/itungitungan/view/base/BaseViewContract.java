package com.kanzankazu.itungitungan.view.base;

import retrofit2.Call;

public interface BaseViewContract {

    //PROGRESS DIALOG
    void showToastView(String message);
    void showSnackbarView(String message);
    void showRetryDialogView(Call call);
    void showProgressDialogView();
    void dismissProgressDialogView();
}
