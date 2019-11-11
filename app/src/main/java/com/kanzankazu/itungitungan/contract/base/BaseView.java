package com.kanzankazu.itungitungan.contract.base;

import android.view.View;
import com.kanzankazu.itungitungan.util.Utils;

public interface BaseView {

    //PROGRESS DIALOG
    void showRetryDialog(Utils.DialogButtonListener listener);

    void showProgressDialog();

    void dismissProgressDialog();

    //TOAST SNACKBAR
    void showToast(String message);

    void showSnackbar(String message);

    void showSnackbar(String message, int snackbarLenght);

    void showSnackbar(String message, View.OnClickListener listener);
}
