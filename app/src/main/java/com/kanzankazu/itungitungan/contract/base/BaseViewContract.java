package com.kanzankazu.itungitungan.contract.base;

import retrofit2.Call;

public interface BaseViewContract {

    //PROGRESS DIALOG
    void showToasts(String message);

    void showSnackbars(String message);

    void showRetryDialogs(Call call);

    void showProgressDialogs();

    void dismissProgressDialogs();
}
