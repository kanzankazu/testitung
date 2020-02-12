package com.kanzankazu.itungitungan.view.base;

public interface BasePresenterContractWOCheckMessage {

    //PROGRESS DIALOG
    void showProgressDialogPresenter();

    void dismissProgressDialogPresenter();

    void onNoConnection(String message);
}
