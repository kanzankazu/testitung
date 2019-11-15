package com.kanzankazu.itungitungan.view.base;

import retrofit2.Call;

public interface BasePresenterContract {

    //PROGRESS DIALOG
    void showProgressDialog();

    void dismissProgressDialog();

    void onNoConnection(Call call);

    boolean checkFailedMessage(String message);
}
