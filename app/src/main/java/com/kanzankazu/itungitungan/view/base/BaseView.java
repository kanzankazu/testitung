package com.kanzankazu.itungitungan.view.base;

import android.view.View;

import com.kanzankazu.itungitungan.util.DialogUtil;

public interface BaseView {

    //PROGRESS DIALOG
    void showRetryDialog(DialogUtil.DialogButtonListener listener);

    void showProgressDialog();

    void dismissProgressDialog();

    //TOAST SNACKBAR
    void showToast(String message);

    void showToast(String message, int snackbarLenght);

    void showSnackbar(String message);

    void showSnackbar(String message, int snackbarLenght);

    void showSnackbar(String message, View.OnClickListener listener);

    void showSnackbarNoConnection();
}
