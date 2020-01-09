package com.kanzankazu.itungitungan.view.base;

import retrofit2.Call;

public interface BasePresenterContractWOCheckMessage {

    //PROGRESS DIALOG
    void showProgressDialoPresenter();

    void dismissProgressDialogPresenter();

    void onNoConnection(Call call);
}
