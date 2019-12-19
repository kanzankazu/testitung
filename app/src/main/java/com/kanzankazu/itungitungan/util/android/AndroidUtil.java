package com.kanzankazu.itungitungan.util.android;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class AndroidUtil {
    public static final int REQ_CODE_PICK_CONTACT = 11;
    public static final int REQ_CODE_PICK_EMAIL_ACCOUNT = 22;

    public static void pickContact(Activity activity) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        activity.startActivityForResult(contactPickerIntent, REQ_CODE_PICK_CONTACT);
    }

    public static Intent pickContactFromFragment(Activity activity) {
        return new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    }

    public static void pickEmailAccount(Activity activity) {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            activity.startActivityForResult(intent, REQ_CODE_PICK_EMAIL_ACCOUNT);
        } catch (ActivityNotFoundException e) {
            Log.e("Lihat", "pickEmailAccount AndroidUtil : " + e.getMessage());
        }
    }

    public static Intent pickEmailAccountFromFragment() {
        return AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
    }

    public static String pickContactResult(int requestCode, int resultCode, Intent data, Activity activity, boolean isPhoneNoElseName) {
        if (requestCode == REQ_CODE_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            Cursor cursor;
            try {
                Uri uri = data.getData();
                cursor = activity.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                Log.d("Lihat", "pickContactResult AndroidUtil : " + cursor);
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                if (isPhoneNoElseName) {
                    return getPhoneNumber62(cursor.getString(phoneIndex));
                } else {
                    return cursor.getString(nameIndex);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String pickEmailAccountResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_EMAIL_ACCOUNT && resultCode == Activity.RESULT_OK) {
            Log.d("Lihat", "pickEmailAccountResult AndroidUtil : " + data);
            return data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        }
        return "";
    }

    private static String getPhoneNumber62(String string) {
        String sub0 = string.substring(0, 1);
        String sub62 = string.substring(0, 2);
        String subPlus62 = string.substring(0, 3);
        String sVal;
        if (sub0.equalsIgnoreCase("0")) {
            sVal = "62" + string.substring(1);
        } else if (sub62.equalsIgnoreCase("62")) {
            sVal = "62" + string.substring(2);
        } else if (subPlus62.equalsIgnoreCase("+62")) {
            sVal = "62" + string.substring(3);
        } else {
            sVal = "62" + string;
        }
        return sVal.replace("-", "");
    }
}
