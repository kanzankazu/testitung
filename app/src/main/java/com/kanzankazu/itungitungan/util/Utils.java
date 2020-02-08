package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseAnalyticsUtil;
import com.kanzankazu.itungitungan.view.logreg.SignInUpActivity;
import com.kanzankazu.itungitungan.view_interface.SnackBarOnClick;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
        mActivity.getSupportActionBar().setElevation(0);
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

    public static void intentTo(Activity mActivity, Class<?> classDestination, Boolean isFinish) {
        if (mActivity != null) {
            Intent intent = new Intent(mActivity, classDestination);
            if (isFinish) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(mActivity);
            mActivity.startActivity(intent);
            if (isFinish) mActivity.finish();
        }
    }

    public static void exitApp() {
        //Intent intent = new Intent(mActivity, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("IS_EXIT", true);
        //mActivity.startActivity(intent);
        //mActivity.finish();
        System.exit(0);
    }

    public static void overridePendingTransition(Activity mActivity) {
        mActivity.overridePendingTransition(0, 0);
    }
    public static void overridePendingTransition(Activity mActivity, int enterAnim, int exitAnim) {
        mActivity.overridePendingTransition(enterAnim, exitAnim);
    }

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
        snackbar.setAction(mActivity.getString(R.string.confirm_login), v -> {
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
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();

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

    public static void goToAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static void goToPlayStore(Activity mActivity) {
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

    public static boolean fileExists(Context context, String fileName) {
        File file = new File(getRootDirPath(context), fileName);
        return (file.exists());
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

    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
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

    public static String getRupiahToString(TextView textView) {
        String s = textView.getText().toString().trim();
        return getRupiahToString(s);
    }

    public static String getRupiahToString(String rupiah) {
        return rupiah.replaceAll("Rp ", "").replaceAll("\\.", "");
    }

    public static String replace(String emailAddress) {
        return emailAddress.replaceAll("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)", "*");
    }

    public static String getInitialName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        } else {
            String[] words = name.split(" ");
            StringBuilder s1 = new StringBuilder();
            for (String word : words) {
                String s = Character.toUpperCase(word.charAt(0)) + " ";
                s1.append(s);
            }
            return s1.toString();
        }
    }

    /**
     * implementation 'com.amulyakhare:com.amulyakhare.textdrawable:1.0.1'
     *
     * @param string = bisa apa saja, dipasang di Imageview dengan (imageView.setImageDrawable(textDrawable))
     * @return TextDrawable
     */
    public static TextDrawable getInitialNameDrawable(String string) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(string);
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(80)
                .height(80)
                .endConfig()
                .roundRect(20);
        return builder.build(Utils.getInitialName(string), color);
    }

    public static void setDrawableImageView(Activity activity, ImageView imageView, @DrawableRes int icon) {
        if (icon != 0) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(activity.getResources().getDrawable(icon));
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
