package com.kanzankazu.itungitungan.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.contract.base.BaseView;
import com.kanzankazu.itungitungan.util.SystemUtil;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.util.dialog.ProgressDialogConnection;

public class BaseFragment extends Fragment implements BaseView {
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
    public void showRetryDialog(Utils.DialogButtonListener listener) {
        Utils.showRetryDialog(mActivity, listener);
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

}
