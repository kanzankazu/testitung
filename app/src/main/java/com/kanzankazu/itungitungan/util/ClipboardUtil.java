package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.widget.Toast;

public class ClipboardUtil {

    public static void clipboardCopy(Activity activity, String s) {
        android.content.ClipboardManager clipMan = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("clip", s);
        clipMan.setPrimaryClip(clipData);
        Toast.makeText(activity, "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
    }

    public String clipboardPaste(Activity activity) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        return item.getText().toString();
    }
}
