package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.kanzankazu.itungitungan.MyApplication;

import java.util.Calendar;
import java.util.List;

public class SystemUtil {

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static void textCaps(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */
        if (focusedView != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static String stringToStars(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    public static Integer stringCount(String s) {
        return s.length();
    }

    public static String stringSpaceToPlus(String s) {
        return s.replace(" ", "+");
    }

    public static void visibileAnim(View view, int visible) {
            view.setVisibility(visible);
    }

    public static void visibileAnim(Context context, View view, int visible, int anim) {
        if (view.getVisibility() != visible) {
            view.setVisibility(visible);
            Animation animation = AnimationUtils.loadAnimation(context, anim);
            view.startAnimation(animation);
        }
    }

    public static boolean visibileView(CheckBox checkBox, int i) {
        if (i == 0) {
            checkBox.setChecked(false);
            return false;
        } else {
            checkBox.setChecked(true);
            return true;
        }
    }

    public static boolean visibileView(Switch aSwitch, int i) {
        if (i == 0) {
            aSwitch.setChecked(false);
            return false;
        } else {
            aSwitch.setChecked(true);
            return true;
        }
    }

    public static boolean visibileView(TextView textView, String s) {
        if (TextUtils.isEmpty(s)) {
            textView.setVisibility(View.GONE);
            textView.setText("");
            return false;
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(s);
            return true;
        }
    }

    public static boolean visibileView(EditText editText, String s) {
        if (TextUtils.isEmpty(s)) {
            editText.setText("");
            return false;
        } else {
            editText.setText(s);
            return true;
        }
    }

    public static boolean visibileView(View parentView, TextView textView, String s) {
        if (!TextUtils.isEmpty(s)) {
            parentView.setVisibility(View.VISIBLE);
            textView.setText(s);
            return true;
        } else {
            parentView.setVisibility(View.GONE);
            textView.setText("");
            return false;
        }
    }

    public static boolean visibleView(View parentView, List<String> ss, TextView... textViews) {
        int stringSize = ss.size();
        int tvLength = textViews.length;

        if (stringSize != 0) {
            if (stringSize == tvLength) {
                for (int i = 0; i < textViews.length; i++) {
                    TextView textView = textViews[i];
                    textView.setText(ss.get(i));
                }
            }
            parentView.setVisibility(View.VISIBLE);
            return true;
        } else {
            parentView.setVisibility(View.GONE);
            return false;
        }
    }

    /**
     * @param parentView
     * @param ss
     * @param textView1
     * @param textView2
     * @return
     */
    public static boolean visibileView(View parentView, List<String> ss, TextView textView1, TextView textView2) {
        int stringSize = ss.size();

        if (stringSize > 0 && stringSize <= 2) {
            textView1.setText(ss.get(0));
            textView1.setText(ss.get(1));
            parentView.setVisibility(View.VISIBLE);
            return true;
        } else {
            parentView.setVisibility(View.GONE);
            return false;
        }
    }

    public static boolean visibileView(View parentView, String s) {
        if (TextUtils.isEmpty(s)) {
            parentView.setVisibility(View.GONE);
            return false;
        } else {
            parentView.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static int roundDouble(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    public static void etReqFocus(Activity activity, String s, EditText editText) {
        editText.setError(s);
        editText.requestFocus();
        SystemUtil.showKeyBoard(activity);
    }

    public static void etReqFocus(Activity activity, EditText editText) {
        editText.requestFocus();
        SystemUtil.showKeyBoard(activity);
    }

    public static void changeColText(int androidRed, TextView textView) {
        if (DeviceDetailUtil.isKitkatBelow()) {
            textView.setTextColor(MyApplication.getContext().getResources().getColor(androidRed));
        } else {
            textView.setTextColor(ContextCompat.getColor(MyApplication.getContext(), androidRed));
        }
    }

    public static void showHideView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void openAnotherApps(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                intent.setData(Uri.parse("market://details?id=" + packageName));
            } catch (ActivityNotFoundException e) {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            }
            context.startActivity(intent);
        }
    }

    public static void openAppRating(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(otherAppActivity.applicationInfo.packageName, otherAppActivity.name);
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            context.startActivity(webIntent);
        }
    }

}
