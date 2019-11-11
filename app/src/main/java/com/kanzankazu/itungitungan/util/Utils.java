package com.kanzankazu.itungitungan.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseAnalyticsUtil;
import com.kanzankazu.itungitungan.view_interface.SnackBarOnClick;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Faisal Bahri on 2019-11-05.
 */
public class Utils {
    public static int widthDisplay, heightDisplay;

    public static boolean showingDialogOnce = false;

    public static void getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthDisplay = size.x;
        heightDisplay = size.y;
    }

    public static int getWidthDisplayDevice() {
        return widthDisplay;
    }

    public static int getToolbarHeight(Activity mActivity) {
        int[] toolbarHeight = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mActivity.obtainStyledAttributes(typedValue.data, toolbarHeight);
        int height = typedArray.getDimensionPixelSize(indexOfAttrTextSize, -1);
        typedArray.recycle();
        return height;
    }

    //REAL FULL DEVICE
    public static int getRealHeightDisplay() {
        System.out.println("HEIGHT REAL = " + heightDisplay);
        return heightDisplay;
    }

    //NON REAL - APP ONLY
    public static int getNonRealHeightDisplay(Activity activity) {

        System.out.println("HEIGHT NON REAL = " + (heightDisplay - getStatusBarHeight(activity)));
        return heightDisplay - getStatusBarHeight(activity);
    }

    public static int getWithoutToolbarHeightDisplay(Activity activity) {
        return getNonRealHeightDisplay(activity) - getToolbarHeight(activity);
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = activity.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

        /*public static void initCustomToolbar(final AppCompatActivity mActivity, Toolbar toolbar) {

            if (!(mActivity instanceof BookingDetailActivity)) { //IF ACTIVITY BESIDE DETAIL
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                View customView = inflater.inflate(R.layout.custom_view_action_bar_booking_step, null);

                toolbar.addView(customView);
                TextView step3Title = customView.findViewById(R.id.tv_title_step_three);
                TextView tvConfirmation = customView.findViewById(R.id.tv_action_step_three);
                TextView step2Title = customView.findViewById(R.id.tv_title_step_two);
                TextView tvPilihPromo = customView.findViewById(R.id.tv_action_step_two);
                ImageView arrow2 = customView.findViewById(R.id.arrow2);

                if (mActivity instanceof BookingAbstractActivity) {
                    step2Title.setTextColor(mActivity.getResources().getColor(R.color.White));
                    tvPilihPromo.setTextColor(mActivity.getResources().getColor(R.color.White));
                    arrow2.setColorFilter(ContextCompat.getColor(mActivity, R.color.White), PorterDuff.Mode.SRC_ATOP);
                    step3Title.setTextColor(mActivity.getResources().getColor(R.color.cyan));
                    tvConfirmation.setTextColor(mActivity.getResources().getColor(R.color.cyan));
                }

            }
        }*/

    public static void setupAppToolbarForActivity(final AppCompatActivity mActivity, Toolbar toolbar) {
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());
    }

    public static void setupAppToolbarForActivity(final AppCompatActivity mActivity, Toolbar toolbar, String title) {
        TextView tvTitle = mActivity.findViewById(R.id.tv_title_toolbar);
        tvTitle.setText(title);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());
    }

    public static void setupAppToolbarForFragment(final AppCompatActivity mActivity, View view, Toolbar toolbar, String title) {
        TextView tvTitle = view.findViewById(R.id.tv_title_toolbar);
        tvTitle.setText(title);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationOnClickListener(view1 -> mActivity.onBackPressed());
    }

    public static void setupToolbarTransparentWithoutTitle(final AppCompatActivity mActivity, Toolbar toolbar) {
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setTitle("");
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());
    }

    public static void intentWithClearTask(Activity mActivity, Class<?> classDestination) {
        if (mActivity != null) {
            Intent intent = new Intent(mActivity, classDestination);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(mActivity);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    public static void exitApp() {
        //Intent intent = new Intent(activity, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("IS_EXIT", true);
        //activity.startActivity(intent);
        //activity.finish();
        System.exit(0);
    }

    public static void overridePendingTransition(Activity mActivity) {
        mActivity.overridePendingTransition(0, 0);
    }

    public static final ButterKnife.Action<View> DISABLE = (view, index) -> view.setEnabled(false);

    public static final ButterKnife.Action<View> ENABLE = (view, index) -> view.setEnabled(true);

    public static void showSnackBar(View view, String message) {

        try {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBarDynamicTime(View view, String message, int millis) {

        try {
            Snackbar snackbar = Snackbar.make(view, message, millis);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showPermissionSnackBar(View view, String message, String button, final SnackBarOnClick snackBarOnClick) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(button, v -> snackBarOnClick.onSnackBarClick());

        snackbar.show();
    }

    public static void showSnackBarToLogin(final Activity mActivity, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(mActivity.getString(R.string.login), v -> {
            mActivity.startActivity(new Intent(mActivity, SignInUpActivity.class));
            mActivity.finish();
        });

        snackbar.show();
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static void closeSoftKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (im != null) {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSnackBar(Activity activity, String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();

    }

    public static void showSnackBar(Activity activity, String message, int snackbarLenght) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, snackbarLenght).show();

    }

    public static void showSnackBar(Activity activity, String message, View.OnClickListener listener) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE).setAction("Action", listener).show();

    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public interface DialogButtonListener {
        void onDialogButtonClick();
    }

    public static void showFetchErrorDialog(Activity activity) {
        try {
            final android.app.Dialog dialog;
            dialog = new android.app.Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_error_dialog);
            dialog.setCancelable(false);

            ImageView ivDialog = dialog.findViewById(R.id.image_booking_complete);
            TextView tvDescDialog = dialog.findViewById(R.id.tv_booking_complete);
            TextView tvCloseDialog = dialog.findViewById(R.id.btn_close_dialog);

            ivDialog.setImageResource(R.drawable.ic_no_internet);
            tvDescDialog.setText("Mohon coba beberapa saat lagi");
            tvCloseDialog.setTextColor(ContextCompat.getColor(activity, R.color.color_blue_A400));
            tvCloseDialog.setText("Ok");
            tvCloseDialog.setOnClickListener(v -> {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                FirebaseAnalyticsUtil.getInstance().firebaseRemoteConfigError();
            });

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorDialog(final Activity activity, String errorMessage, boolean isShow, final DialogButtonListener dialogButtonListener) {
        try {
            final android.app.Dialog dialog;
            dialog = new android.app.Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_error_dialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            ImageView ivDialog = dialog.findViewById(R.id.image_booking_complete);
            TextView tvDescDialog = dialog.findViewById(R.id.tv_booking_complete);
            TextView tvCloseDialog = dialog.findViewById(R.id.btn_close_dialog);

            ivDialog.setImageResource(R.drawable.ic_no_internet);
            tvDescDialog.setText("Gagal terhubung jaringan,\n Silahkan coba kembali.");
            tvCloseDialog.setTextColor(ContextCompat.getColor(activity, R.color.color_blue_A400));
            tvCloseDialog.setText(activity.getString(R.string.message_dialog_retry));
            tvCloseDialog.setOnClickListener(v -> {
                dialog.dismiss();
                dialogButtonListener.onDialogButtonClick();
            });

            if (!isShow) {
                dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double roundToHalf(double d) { // Result : 2.1 -> 2, 2,2 -> 2, 2.3 -> 2.5, 2.4 -> 2.5
        return Math.round(d * 2) / 2.0;
    }

    public static float roundToUp(float newValue) { // Result : 2.1 -> 2.5 , 2,2 -> 2.5 , 2.3 -> 2.5, 2.4 -> 2.5

        if (newValue != 0.00 && newValue != 5.00) { //Validate if the input either 0.00 or 5.00 the output will still the same
            newValue = (float) (Math.round(newValue - 0.5) + 0.5);
        }

        return newValue;
    }

    public static boolean isGpsEnabled(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void printJsonObject(String tag, Object object) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(object);
        System.out.println(tag + jsonObject);
    }

    public static void openDeviceSetting(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
    }

    public static void openLocationMode(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    public static void goToSumoAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static String setRupiah(String inputCredit) {
        String sCredit = null;
        int credit;
        try {
            if (inputCredit.contains(".")) {
                sCredit = inputCredit.substring(0, inputCredit.indexOf("."));
                credit = Integer.parseInt(sCredit);
                sCredit = "Rp " + NumberFormat.getNumberInstance(Locale.US).format(credit).replace(',', '.');
            } else {
                credit = Integer.parseInt(inputCredit);
                sCredit = "Rp " + NumberFormat.getNumberInstance(Locale.US).format(credit).replace(',', '.');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sCredit;
    }

    public static void showRetryDialog(Activity mActivity, final DialogButtonListener dialogButtonListener) {
        if (!mActivity.isFinishing()) {
            android.app.Dialog dialog = new AlertDialog.Builder(mActivity)
                    .setView(mActivity.getLayoutInflater().inflate(R.layout.layout_error_dialog, null))
                    .setCancelable(false)
                    .show();

            ImageView ivDialog = ButterKnife.findById(dialog, R.id.image_booking_complete);
            TextView tvDescDialog = ButterKnife.findById(dialog, R.id.tv_booking_complete);
            TextView tvCloseDialog = ButterKnife.findById(dialog, R.id.btn_close_dialog);

            ivDialog.setImageResource(R.drawable.ic_no_internet);
            tvDescDialog.setText("Gagal terhubung jaringan,\n Silahkan coba kembali.");
            tvCloseDialog.setTextColor(ContextCompat.getColor(mActivity, R.color.cyan));
            tvCloseDialog.setText(mActivity.getResources().getString(R.string.message_dialog_retry));
            tvCloseDialog.setOnClickListener(v -> {
                dialogButtonListener.onDialogButtonClick();
                dialog.dismiss();
            });
        }
    }

    public static void showConfirmationDialog(Activity mActivity, final DialogButtonListener dialogButtonListener, String dialogTitle, String dialogDescription) {
        if (!mActivity.isFinishing()) {
            android.app.Dialog dialog = new AlertDialog.Builder(mActivity)
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

    public static void goToSumoPlayStore(Activity mActivity) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mActivity.startActivity(myAppLinkToMarket);
            FirebaseAnalyticsUtil.getInstance().firebaseAppRateClickedEvent();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, " unable to find market app", Toast.LENGTH_LONG).show();
            FirebaseAnalyticsUtil.getInstance().firebaseAppOpenPlayStoreErrorEvent();
        }
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(Long currentBytes, Long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    public static String getBytesToMBString(Long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    public static boolean fileExists(Context context, String fileName) {
        File file = new File(getRootDirPath(context), fileName);
        return (file.exists());
    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static String getDocumentIdNumber(String documentId) {
        String[] split = documentId.split("IMC");
        return split[1];
    }

    public static CharSequence[] convertToCharSequenceArray(List<CharSequence> radioButtonData) {
        return radioButtonData.toArray(new
                CharSequence[radioButtonData.size()]);
    }

    public static String convertToKm(String km) {
        //  ex : 12.345,6 replace all "." with "" and "," with ".", enable parsing to double
        String sKm = km.replaceAll("\\.", "").replaceAll("\\,", ".");
        // ex : 12500.6 parse to double -> 12.500.6 replace all "." with "," and replace first found char of "." with ","
        return String.format("%s KM", String.valueOf(NumberFormat.getNumberInstance(Locale.UK).format(Double.parseDouble(sKm))).replace(".", ",").replaceFirst(",", "."));
    }

    public static String convertToKmWithNumberOnly(String km) {
        //  ex : 12.345,6 replace all "." with "" and "," with ".", enable parsing to double
        String sKm = km.replaceAll("\\.", "").replaceAll("\\,", ".");
        // ex : 12500.6 parse to double -> 12.500.6 replace all "." with "," and replace first found char of "." with ","
        return String.format("%s", String.valueOf(NumberFormat.getNumberInstance(Locale.UK).format(Double.parseDouble(sKm))).replace(".", ",").replaceFirst(",", "."));
    }

    public static Double convertToKmDouble(String km) {
        String res = km.replaceAll("\\.", "").replaceAll("\\,", ".");
        return Double.parseDouble(res);
    }

    public static void requestWriteExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to Write External Storage Granted");
            } else {
                Log.i("Permission", "Permission to Write External Storage Revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
            }
        } else {
            Log.i("Permission", "Permission Write External Storage Granted");
        }
    }

    public static void requestReadExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to Read External Storage Granted");
            } else {
                Log.i("Permission", "Permission to Read External Storage Revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        } else {
            Log.i("Permission", "Permissions are granted");
        }
    }

    public static void requestCameraPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission to Access Camera Granted");
            } else {
                Log.i("Permission", "Permission to Access Camera Revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 13);
            }
        } else {
            Log.i("Permission", "Permission to Access Camera Granted");
        }
    }

    public static void compressImage(String mCurrentPhotoPath) {
        File file = new File(Uri.parse(mCurrentPhotoPath).getPath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 1440, 1440, true);

        try {
            ExifInterface ei = new ExifInterface(Uri.parse(mCurrentPhotoPath).getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    original = rotateImage(original, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    original = rotateImage(original, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    original = rotateImage(original, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    //original = original;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            original.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        //create temp file bakal generate unique ID dibelakang (setelah timestamp_) (source:https://stackoverflow.com/questions/17150597/file-createtempfile-vs-new-file)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public interface IntroductionButtonListener {
        void onFirstButtonClick();

        void onSecondButtonClick();
    }

    public static void showIntroductionDialog(Activity mActivity, final IntroductionButtonListener introductionButtonListener, String imageUrl, String title, String message, String titleButton1, String titleButton2,
                                              boolean isCancelable, int assetHardCode) {
        if (!mActivity.isFinishing()) {
            AlertDialog alertDialog;
            View dialogView = mActivity.getLayoutInflater().inflate(R.layout.popup_introduction, null);
            final AlertDialog.Builder popupPromo = new AlertDialog.Builder(mActivity);
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
                Glide.with(mActivity).load(assetHardCode).into(imgIntro);
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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String htmlSource) {
        if (htmlSource != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //FROM_HTML_MODE_LEGACY ->Separate block-level elements with blank lines
                return Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(htmlSource);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml("");
            }
        }
    }

    public static void convertMapStringToJsonObject(Map<String, String> map, String printTag) {
        Gson gson = new Gson();
        System.out.println("gson to json object " + printTag + " : " + gson.toJsonTree(map).getAsJsonObject());
    }

    public static void convertMapObjectToJsonObject(Map<String, Object> map, String printTag) {
        Gson gson = new Gson();
        System.out.println("gson to json object " + printTag + " : " + gson.toJsonTree(map).getAsJsonObject());
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void sendJubelWhatsapp(Context context, String url) {
        try {
            Intent intentAction = new Intent(Intent.ACTION_VIEW);
            intentAction.setData(Uri.parse(url));
            context.startActivity(intentAction);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "apps not found", Toast.LENGTH_LONG).show();
        }
    }

    public static void openUrlWithChromeCustomTabs(String url, Context context) {
        Uri uri = Uri.parse(url);

        // create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // Begin customizing
        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.navy_blue));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.navy_blue));

        // set start and exit animations
        intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // launch the url
        customTabsIntent.launchUrl(context, uri);
    }

    public static String convertListStringToString(List<String> stringList, String demiliter) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        return TextUtils.join(demiliter, myStringList);
    }

       /* public static String convertListObjToListString(List<CategoryType> categoryTypes, Boolean isSc) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < categoryTypes.size(); i++) {
                if (isSc) {
                    list.add(categoryTypes.get(i).getScName().replace(",","|"));
                } else {
                    list.add(categoryTypes.get(i).getStName().replace(",","|"));
                }
            }

            return Utils.convertListStringToString(list, "|");
        }*/
}
