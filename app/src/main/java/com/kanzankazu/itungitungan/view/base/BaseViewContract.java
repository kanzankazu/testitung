package com.kanzankazu.itungitungan.view.base;

public interface BaseViewContract {

    //PROGRESS DIALOG
    void showToastView(String message);
    void showSnackbarView(String message);
    void showRetryDialogView();
    void showProgressDialogView();
    void dismissProgressDialogView();
}
