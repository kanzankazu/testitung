package com.kanzankazu.itungitungan.view.util.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.StringTokenizer;

public class NumberTextWatcherForThousand implements TextWatcher {

    EditText editText;

    public NumberTextWatcherForThousand(EditText editText) {
        this.editText = editText;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editText.getText().toString().trim().length() <= 0) {
            editText.setText("0");
            editText.selectAll();
        }

        try {
            editText.removeTextChangedListener(this);
            String value = editText.getText().toString();

            if (value != null && !value.equals("")) {

                if (value.startsWith(".")) {
                    editText.setText("0.");
                }
                if (value.startsWith("0") && !value.startsWith("0.")) {
                    editText.setText("");

                }

                String str = editText.getText().toString().replaceAll(",", "");
                if (!value.equals("")) {
                    editText.setText(getDecimalFormattedString(str));
                }
                editText.setSelection(editText.getText().toString().length());
            }
            editText.addTextChangedListener(this);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            editText.addTextChangedListener(this);
        }

    }

    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0) {
                    str3 = str3 + "." + str2;
                }
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    public static String trimCommaOfString(String string) {
        //String returnString;
        String s;
        if (string.contains(".")) {
            String[] split = string.split(".");
            s = split[0];
            if (s.contains(",")) {
                return s.replace(",", "");
            } else {
                return s;
            }
        } else {
            s = string;
            if (s.contains(",")) {
                return s.replace(",", "");
            } else {
                return s;
            }
        }


    }
}
