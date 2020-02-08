package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.AnimatorRes;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kanzankazu.itungitungan.R;

import java.util.List;

import butterknife.ButterKnife;

public class DialogUtil {

    /*
    *
    *
    custom_alert_dialog
    *
    <?xml version="1.0" encoding="utf-8"?>
    <CheckedTextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@android:id/text1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:minHeight="?android:attr/listPreferredItemHeight"
    android:textAppearance="?android:attr/textAppearanceListItemSmall"
    android:textColor="?android:attr/textColorAlertDialogListItem"
    style="@style/TextLabelStyle"
    android:gravity="center_vertical"
    android:paddingLeft="16dp"
    android:paddingRight="16dp"
    android:checkMark="@null"
    android:drawableLeft="?android:attr/listChoiceIndicatorSingle"
    android:drawableRight="@null"
    android:ellipsize="marquee" />
    * */
    public static AlertDialog setupRadioAlertDialog(Activity activity, String title, CharSequence[] radioButtonData, int checkedIndex, int identifier, String mode, DialogRadioListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        TextView titleTv = new TextView(activity);
        titleTv.setTypeface(ResourcesCompat.getFont(activity, R.font.avenir_next_ltpro_demi));
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        titleTv.setTypeface(Typeface.DEFAULT_BOLD);
        titleTv.setTextColor(activity.getResources().getColor(R.color.navy_blue));
        titleTv.setText(title);
        titleTv.setPadding(48, 48, 48, 24);

        dialogBuilder.setCustomTitle(titleTv);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(activity, R.layout.custom_alert_dialog, radioButtonData);

        dialogBuilder.setSingleChoiceItems(adapter, checkedIndex, (dialogInterface, index) -> {
            Utils.closeSoftKeyboard(activity);
            listener.onRadioButtonClick(index, identifier, mode);
            dialogInterface.dismiss();
        });
        return dialogBuilder.create();
    }

    public static void setupCustomAlertDialog(Activity activity, @LayoutRes int layout, @AnimatorRes int anim, Boolean isCancelable, DialogCustomListener listener) {
        View dialogView = activity.getLayoutInflater().inflate(layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(isCancelable);
        alertDialog.setCanceledOnTouchOutside(isCancelable);

        if (anim != 0) {
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.AnimationRTL;
            }
        }

        listener.customDialogContent(dialogView, alertDialog);

        alertDialog.show();

    }

    /**
     * @param mActivity                  inisialisasi class
     * @param introductionButtonListener action untuk titleButton1 & titleButton2 / (tombol silang, jika titleButton 2 kosong), dan jika tidak di isi secara default akan menutup popup
     * @param imageUrl                   gambar yang di tampilkan (berbentuk string URL)
     * @param title                      text untuk judul popup dialog
     * @param message                    text untuk isi dari popup dialog
     * @param titleButton1               text untuk tombol utama
     * @param titleButton2               text untuk tombol ke 2
     * @param isCancelable               apakah bisa di tutup, dan jika bisa akan ada tanda silang di pojok kanan atas
     * @param assetHardCode              gambar yang di tampilkan jika `imageUrl` tidak ada (berbentuk int drawable / atau resourceId)
     */
    public static void showIntroductionDialog(Activity mActivity, String imageUrl, String title, String message, String titleButton1, String titleButton2, boolean isCancelable, int assetHardCode, IntroductionButtonListener introductionButtonListener) {
        if (!mActivity.isFinishing()) {
            android.app.AlertDialog alertDialog;
            View dialogView = mActivity.getLayoutInflater().inflate(R.layout.popup_introduction, null);
            final android.app.AlertDialog.Builder popupPromo = new android.app.AlertDialog.Builder(mActivity);
            popupPromo.setView(dialogView);

            TextView tvIntroTitle = ButterKnife.findById(dialogView, R.id.tv_popup_intro_title);
            ImageView imgIntro = ButterKnife.findById(dialogView, R.id.img_popup_intro);
            ImageView imgClose = ButterKnife.findById(dialogView, R.id.img_popup_close);
            TextView tvIntroMessage = ButterKnife.findById(dialogView, R.id.tv_popup_intro_message);
            TextView tvPopupButton = ButterKnife.findById(dialogView, R.id.tv_popup_button);
            TextView tvPopupButton2 = ButterKnife.findById(dialogView, R.id.tv_popup_button2);
            ScrollView svIntroDesc = ButterKnife.findById(dialogView, R.id.sv_intro_desc);

            LinearLayout llPopup = ButterKnife.findById(dialogView, R.id.ll_popup);
            llPopup.setBackgroundColor(Color.parseColor("#1e3559"));
            imgIntro.setBackgroundColor(Color.parseColor("#1e3559"));
            tvIntroMessage.setTextColor(mActivity.getResources().getColor(R.color.color_off_white));
            tvIntroTitle.setTextColor(mActivity.getResources().getColor(R.color.color_off_white));

            tvPopupButton.setText(titleButton1);
            tvPopupButton2.setText(titleButton2);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(mActivity).load(imageUrl).into(imgIntro);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
                imgIntro.setLayoutParams(layoutParams);
                if (assetHardCode != -1) {
                    Glide.with(mActivity).load(assetHardCode).into(imgIntro);
                } else {
                    Glide.with(mActivity).load(R.mipmap.ic_launcher).into(imgIntro);
                }
            }

            imgClose.setVisibility(isCancelable ? View.VISIBLE : View.GONE);

            //Hide Title if empty
            if (title != null) {
                if (title.isEmpty()) {
                    tvIntroTitle.setVisibility(View.GONE);
                } else {
                    tvIntroTitle.setText(title);
                    tvIntroTitle.setVisibility(View.VISIBLE);
                }
            }
            //Hide ScrollView that contain intro description if description empty
            if (message.isEmpty()) {
                svIntroDesc.setVisibility(View.GONE);
            } else {
                svIntroDesc.setVisibility(View.VISIBLE);
                tvIntroMessage.setText(message);
            }
            //Hide second button if second button title is empty
            if (titleButton2.isEmpty()) {
                tvPopupButton2.setVisibility(View.GONE);
            } else {
                tvPopupButton2.setVisibility(View.VISIBLE);
                tvPopupButton2.setText(titleButton2);
            }

            alertDialog = popupPromo.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            imgClose.setOnClickListener(view -> {
                alertDialog.dismiss();
                Utils.showingDialogOnce = true;
            });
            tvPopupButton.setOnClickListener(v -> {
                alertDialog.dismiss();
                introductionButtonListener.onFirstButtonClick();
                Utils.showingDialogOnce = true;
            });
            tvPopupButton2.setOnClickListener(v -> {
                alertDialog.dismiss();
                introductionButtonListener.onSecondButtonClick();
            });
        }
    }

    public static void showConfirmationDialog(Activity mActivity, String dialogTitle, String dialogDescription, DialogButtonListener dialogButtonListener) {
        if (!mActivity.isFinishing()) {
            android.app.Dialog dialog = new android.app.AlertDialog.Builder(mActivity)
                    .setView(mActivity.getLayoutInflater().inflate(R.layout.layout_confirmation_dialog, null))
                    .show();

            TextView tvConfirmationDialogTitle = ButterKnife.findById(dialog, R.id.tv_confirmation_text);
            TextView tvConfirmationDialogDescription = ButterKnife.findById(dialog, R.id.tv_confirmation_description);
            TextView tvCloseDialog = ButterKnife.findById(dialog, R.id.btn_close_dialog);
            TextView tvCloseDialogOk = ButterKnife.findById(dialog, R.id.btn_close_dialog_OK);

            tvConfirmationDialogTitle.setText(dialogTitle);
            if (dialogDescription.isEmpty()) {
                tvConfirmationDialogDescription.setVisibility(View.GONE);
            } else {
                tvConfirmationDialogDescription.setVisibility(View.VISIBLE);
                tvConfirmationDialogDescription.setText(dialogDescription);
            }
            tvCloseDialog.setOnClickListener(v -> dialog.dismiss());
            tvCloseDialogOk.setOnClickListener(view -> {
                dialogButtonListener.onDialogButtonClick();
                dialog.dismiss();
            });
        }
    }

    public static void showRetryDialog(Activity mActivity, DialogButtonListener dialogButtonListener) {
        if (!mActivity.isFinishing()) {
            Dialog dialog = new AlertDialog.Builder(mActivity)
                    .setView(mActivity.getLayoutInflater().inflate(R.layout.layout_error_dialog, null))
                    .setCancelable(false)
                    .show();

            ImageView ivDialog = ButterKnife.findById(dialog, R.id.image_booking_complete);
            TextView tvDescDialog = ButterKnife.findById(dialog, R.id.tv_booking_complete);
            TextView tvCloseDialog = ButterKnife.findById(dialog, R.id.btn_close_dialog);

            ivDialog.setImageResource(R.drawable.ic_no_internet);
            tvDescDialog.setText("Gagal terhubung jaringan,\n Silahkan coba kembali.");
            tvCloseDialog.setTextColor(ContextCompat.getColor(mActivity, R.color.cyan));
            tvCloseDialog.setText(mActivity.getResources().getString(R.string.confirm_retry));
            tvCloseDialog.setOnClickListener(v -> {
                dialogButtonListener.onDialogButtonClick();
                dialog.dismiss();
            });
        }
    }

    public static void showYesNoDialog(Activity activity, String title, String message, DialogStandartTwoListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        AlertDialog show = alertDialog.show();
        alertDialog.setPositiveButton(activity.getString(R.string.confirm_yes), (dialogInterface, i) -> {
            show.dismiss();
            listener.onClickButtonOne();
        });
        alertDialog.setNegativeButton(activity.getString(R.string.confirm_no), (dialogInterface, i) -> {
            show.dismiss();
            listener.onClickButtonTwo();
        });
    }

    public static void listDialog(Activity mActivity, @ArrayRes int i, DialogInterface.OnClickListener listener) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mActivity);
        alert.setItems(i, listener);
        alert.show();
    }

    public static void listDialog(Activity mActivity, CharSequence[] charSequences, DialogInterface.OnClickListener listener) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mActivity);
        alert.setItems(charSequences, listener);
        alert.show();
    }

    public static void listMultipleChoiceDialog(Activity mActivity, @ArrayRes int i, boolean[] booleans, DialogInterface.OnMultiChoiceClickListener listener) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mActivity);
        alert.setMultiChoiceItems(i, booleans, listener);
        alert.show();
    }

    public static void listMultipleChoiceDialog(Activity mActivity, CharSequence[] charSequences, boolean[] booleans, DialogInterface.OnMultiChoiceClickListener listener) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mActivity);
        alert.setMultiChoiceItems(charSequences, booleans, listener);
        alert.show();
    }

    public static CharSequence[] convertListStrinToCharSequenceArray(List<String> list) {
        return list.toArray(new CharSequence[list.size()]);
    }

    public static CharSequence[] convertToCharSequenceArray(List<CharSequence> list) {
        return list.toArray(new CharSequence[list.size()]);
    }

    public interface DialogStandartListener {
        void onClickButton();
    }

    public interface DialogStandartTwoListener {
        void onClickButtonOne();

        void onClickButtonTwo();
    }

    public interface DialogRadioListener {
        void onRadioButtonClick(int index, int identifier, String mode);
    }

    public interface DialogCustomListener {
        void customDialogContent(View view, AlertDialog mAlertDialog);
    }

    public interface DialogButtonListener {
        void onDialogButtonClick();
    }

    public interface IntroductionButtonListener {

        void onFirstButtonClick();

        void onSecondButtonClick();

    }

}
