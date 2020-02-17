package com.kanzankazu.itungitungan.util;

import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kanzankazu.itungitungan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidUtil {

    private static boolean passwordShown = false;

    public static boolean isMatch(String string1, String string2) {
        return string1.equals(string2);
    }

    public static boolean isEmptyField(String stringValue) {
        return TextUtils.isEmpty(stringValue);
    }

    public static boolean isEmptyField(EditText editText) {
        String s1 = editText.getText().toString().trim();
        return TextUtils.isEmpty(s1);
    }

    public static boolean isEmptyField(String messageError, EditText... editTexts) {
        Collection<Integer> listStat = new ArrayList<>();
        for (EditText editText : editTexts) {
            if (isEmptyField(editText)) {
                errorET(editText, messageError);
                listStat.add(0);
            } else {
                listStat.add(1);
            }
        }

        int frequency0 = Collections.frequency(listStat, 0);
        int frequency1 = Collections.frequency(listStat, 1);

        return editTexts.length != frequency1;
    }

    public static boolean isEmptyField(@NotNull String messageError, @Nullable TextInputLayout textInputLayout, @Nullable EditText editText, Boolean isFocus) {
        return isEmptyField(messageError, textInputLayout, editText, isFocus, (ActionTrueFalseListener) null);
    }

    public static boolean isEmptyField(@NotNull String messageError, @Nullable TextInputLayout textInputLayout, @Nullable EditText editText, Boolean isFocus, @Nullable ImageButton clearButton) {
        if (isEmptyField(messageError, textInputLayout, editText, isFocus, (ActionTrueFalseListener) null)) {
            clearButton.setVisibility(View.GONE);
            return true;
        } else {
            clearButton.setVisibility(View.VISIBLE);
            return false;
        }
    }

    public static boolean isEmptyField(@NotNull String messageError, @Nullable TextInputLayout textInputLayout, @Nullable EditText editText, Boolean isFocus, ActionTrueFalseListener listener) {
        if (isEmptyField(editText.getText().toString().trim())) {
            textInputLayout.setError(messageError);
            textInputLayout.setErrorEnabled(true);

            if (isFocus)
                editText.requestFocus();

            if (listener != null)
                listener.onTrue();

            return true;
        } else {
            textInputLayout.setErrorEnabled(false);

            if (listener != null)
                listener.onFalse();

            return false;
        }
    }

    public static boolean isEmptyFieldInt(EditText editText) {
        String s = editText.getText().toString().trim();

        if (TextUtils.isEmpty(s)) {
            editText.setText(0);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmptyFieldInts(EditText... editTexts) {
        Collection<Integer> listStat = new ArrayList<>();
        for (EditText editText : editTexts) {
            if (isEmptyFieldInt(editText)) {
                listStat.add(0);
            } else {
                listStat.add(1);
            }
        }

        int frequency0 = Collections.frequency(listStat, 0);
        int frequency1 = Collections.frequency(listStat, 1);

        return editTexts.length != frequency1;//true jika tidak cocok
    }

    public static boolean isEmail(String s1) {
        return Patterns.EMAIL_ADDRESS.matcher(s1).matches();
    }

    public static boolean isEmail(String message, TextInputLayout textInputLayout, EditText editText, Boolean isFocus) {
        if (isEmail(editText.getText().toString().trim())) {
            return true;
        } else {
            textInputLayout.setError(message);
            textInputLayout.setErrorEnabled(true);
            if (isFocus) editText.requestFocus();
            return false;
        }
    }

    public static boolean isTimebiggerThan(String time1, String time2) {
        String hhmmss1[] = time1.split(":");
        String hhmmss2[] = time2.split(":");
        for (int i = 0; i < hhmmss1.length; i++) {
            if (Integer.parseInt(hhmmss1[i]) > Integer.parseInt(hhmmss2[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTimesmallerThan(String time1, String time2) {
        String hhmmss1[] = time1.split(":");
        String hhmmss2[] = time2.split(":");
        for (int i = 0; i < hhmmss1.length; i++) {
            if (Integer.parseInt(hhmmss1[i]) < Integer.parseInt(hhmmss2[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidateIP(final String ip4) {
        String IPADDRESS_PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip4);
        return !matcher.matches();
    }

    public static boolean isPhoneNumber(String message, TextInputLayout textInputLayout, EditText editText, Boolean isFocus) {
        if (!isPhoneNumber(editText.getText().toString())) {
            textInputLayout.setError(message);
            textInputLayout.setErrorEnabled(true);
            if (isFocus) editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPhoneNumber(final String num) {
        String PHONE_NUMBER_PATTERN = "^[+]?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    public static boolean isEmailOrPhone(final String errorMessage, TextInputLayout textInputLayout, EditText editText) {
        String s = editText.getText().toString().trim();
        if (isEmail(s) || isPhoneNumber(s)) {
            textInputLayout.setErrorEnabled(false);
            return true;
        } else {
            textInputLayout.setError(errorMessage);
            textInputLayout.setErrorEnabled(true);
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isLinkUrl(String s) {
        if (s.matches("(?i).*http://.*") || s.matches("(?i).*https://.*")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLenghtCharOver(final String errorMessage, TextInputLayout textInputLayout, EditText editText, int minChar) {
        String s = editText.getText().toString().trim();
        if (isLenghtCharOver(s, minChar)) {
            textInputLayout.setErrorEnabled(false);
            return true;
        } else {
            textInputLayout.setError(errorMessage);
            textInputLayout.setErrorEnabled(true);
            return false;
        }
    }

    public static boolean isLenghtCharOver(EditText editText, int minChar) {
        String s = editText.getText().toString().trim();
        return isLenghtCharOver(s, minChar);
    }

    public static boolean isLenghtCharOver(String stringValue, int minChar) {
        return stringValue.length() > minChar;
    }

    public static boolean isRadioGroupChecked(RadioGroup radioGroup) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            return true;
        }
    }

    public static String getPhoneNumber62(String phoneNumber) {
        //String string = editText.getText().toString();
        String sub0 = phoneNumber.substring(0, 1);
        String sub62 = phoneNumber.substring(0, 2);
        String subPlus62 = phoneNumber.substring(0, 3);
        String sVal = null;
        if (sub0.equalsIgnoreCase("0")) {
            sVal = "62" + phoneNumber.substring(1);
        } else if (sub62.equalsIgnoreCase("62")) {
            sVal = "62" + phoneNumber.substring(2);
        } else if (subPlus62.equalsIgnoreCase("+62")) {
            sVal = "62" + phoneNumber.substring(3);
        } else {
            sVal = "62" + phoneNumber;
        }
        return sVal.replace("-", "");
    }

    public static String getPhoneNumberSuffix(String countryPhoneCode, String phoneNumber) {
        //String string = editText.getText().toString();
        String sub0 = phoneNumber.substring(0, 1);
        String subCode = phoneNumber.substring(0, 2);
        String subPlusCode = phoneNumber.substring(0, 3);
        String sVal = null;
        if (sub0.equalsIgnoreCase("0")) {
            sVal = countryPhoneCode + phoneNumber.substring(1);
        } else if (subCode.equalsIgnoreCase(countryPhoneCode)) {
            sVal = countryPhoneCode + phoneNumber.substring(2);
        } else if (subPlusCode.equalsIgnoreCase("+" + countryPhoneCode)) {
            sVal = countryPhoneCode + phoneNumber.substring(3);
        } else {
            sVal = countryPhoneCode + phoneNumber;
        }
        return sVal.replace("-", "");
    }

    public static float getPercent(long number, long total) {
        return (float) number / (float) total * 100;
    }

    public static void errorET(EditText editText, CharSequence messageError) {
        editText.setError(messageError);
        editText.requestFocus();
    }

    public static void showHidePassword(EditText editText, ImageButton imageButton) {
        if (passwordShown) {//tidak terlihat
            passwordShown = false;
            editText.setInputType(129);//edittest
            editText.setTypeface(Typeface.SANS_SERIF);
            imageButton.setImageResource(R.drawable.ic_visibility_off);
        } else {//terlihat
            passwordShown = true;
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageButton.setImageResource(R.drawable.ic_visibility);
        }
    }

    public static void editTextActionGO(EditText editText, editTextActionListener listener) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    listener.onAction();
                }
                return handled;
            }
        });
    }

    public static void setEditTextMaxLenght(EditText editText, int maxLength) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(fArray);
    }

    public static void creditCardTextWatcher(EditText editText) {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Change this to what you want... ' ', '-' etc..
                char space = ' ';

                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
            }
        };

        editText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                editText.addTextChangedListener(textWatcher);
            } else {
                editText.removeTextChangedListener(textWatcher);
            }
        });

    }

    public interface ActionTrueFalseListener {
        void onTrue();

        void onFalse();
    }

    public interface editTextActionListener {
        void onAction();
    }
}
