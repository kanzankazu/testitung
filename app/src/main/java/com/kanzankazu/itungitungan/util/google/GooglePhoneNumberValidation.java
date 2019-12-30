package com.kanzankazu.itungitungan.util.google;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.kanzankazu.itungitungan.R;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * return string(phonenumber) if true
 * return false if cancel
 *
 * GRADLE Implement
 * implementation 'com.hbb20:ccp:2.1.2'
 * implementation 'com.google.android.gms:play-services:12.0.1'
 * implementation 'com.google.android.gms:play-services-auth:12.0.1'
 * implementation 'com.google.firebase:firebase-auth:12.0.1'
 *
 * xml Data
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:background="@color/colorPrimary"
  android:gravity="center"
  android:paddingStart="30dp"
  android:paddingEnd="30dp"
  android:orientation="vertical">

      <TextView
      android:id="@+id/tvValidPhoneNmbrInfo"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginTop="20dp"
      android:text="Information Send Code"
      android:textAllCaps="true"
      android:textColor="@color/white"
      android:textStyle=""
      android:visibility="visible" />

      <android.support.v7.widget.CardView
      android:layout_width="match_parent"
      android:layout_height="55dp"
      android:layout_marginTop="20dp"
      app:cardCornerRadius="5dp"
      app:cardElevation="1dp">

      <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:layout_margin="5dp"
      android:animateLayoutChanges="true"
      android:orientation="horizontal">

      <com.hbb20.CountryCodePicker
      android:id="@+id/ccpValidPhoneNmbr"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_gravity="center_vertical"
      app:ccpDialog_keyboardAutoPopup="false"
      app:ccpDialog_searchEditTextTint="@color/colorPrimary"
      app:ccpDialog_showCloseIcon="true"
      app:ccp_areaCodeDetectedCountry="true"
      app:ccp_autoDetectCountry="true"
      app:ccp_defaultNameCode="ID"
      app:ccp_flagBorderColor="@color/gray_very_light"
      app:ccp_rememberLastSelection="true"
      app:ccp_selectionMemoryTag="anyDifferentString" />

      <EditText
      android:id="@+id/etValidPhoneNmbr"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="@null"
      android:layout_gravity="center_vertical"
      android:editable="false"
      android:gravity="left"
      android:hint="Enter Phone Number"
      android:imeOptions="actionGo"
      android:inputType="phone"
      android:maxLength="15"
      android:singleLine="true" />

      </LinearLayout>
      </android.support.v7.widget.CardView>

      <android.support.v7.widget.CardView
      android:id="@+id/cvValidPhoneNmbrSubmit"
      android:layout_width="match_parent"
      android:layout_height="55dp"
      android:layout_marginTop="15dp"
      android:backgroundTint="@color/colorPrimaryDark"
      app:cardCornerRadius="5dp"
      app:cardElevation="5dp">

      <LinearLayout
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:layout_margin="5dp"
      android:animateLayoutChanges="true"
      android:orientation="horizontal">

      <TextView
      android:id="@+id/tvValidPhoneNmbrSubmit"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:gravity="center"
      android:singleLine="true"
      android:text="NEXT"
      android:textAllCaps="true"
      android:textStyle="bold"
      android:textColor="@color/white" />

      </LinearLayout>
      </android.support.v7.widget.CardView>

      <TextView
      android:id="@+id/tvValidPhoneNmbrResendCode"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginTop="10dp"
      android:padding="10dp"
      android:text="RE-SEND VERIFICATION CODE"
      android:textAllCaps="true"
      android:textColor="@color/blue"
      android:textStyle="" />

      <LinearLayout
      android:id="@+id/llValidPhoneNmbrBack"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:animateLayoutChanges="true"
      android:gravity="center"
      android:orientation="horizontal"
      android:padding="10dp">

      <ImageView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:src="@drawable/ic_arrow_back_white_24dp"
      android:visibility="visible" />

      <TextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="back"
      android:textAllCaps="true"
      android:textColor="@color/white" />

      </LinearLayout>

      <TextView
      android:id="@+id/tvValidPhoneNmbrAppVersion"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginTop="10dp"
      android:padding="10dp"
      android:textSize="10dp"
      android:textStyle="bold"
      android:text="version"
      android:textColor="@color/colorPrimaryDark" />

  </LinearLayout>
 */
public class GooglePhoneNumberValidation extends AppCompatActivity {
    public static final int REQ_CODE_G_PHONE_VALIDATION = 123;
    private static final long timeoutDuration = 60L;
    private static final int UI_LOGIN = 0;
    private static final int UI_VERIFY = 1;
    /**
     * CountryCodePicker
     */
    private CountryCodePicker ccpValidPhoneNmbrfvbi;
    /**
     * UI
     */
    private EditText etValidPhoneNmbrfvbi;
    private LinearLayout llValidPhoneNmbrSubmitfvbi;
    private TextView tvValidPhoneNmbrSubmitfvbi;
    private TextView tvLoginResendCodefvbi;
    private TextView tvValidPhoneNmbrInfofvbi;
    private TextView tvValidPhoneNmbrAppVersionfvbi;
    private LinearLayout llValidPhoneNmbrBackfvbi;
    /**
     * Variable
     */
    private String appVersionName, appVersionCode, appName, appPkg;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String phoneNumberSavedwPlus;
    private String phoneNumberSavedwoPlus;
    private String mVerificationId;
    private boolean isLogin, isVerify;
    private ProgressDialog progressDialogSubmit;

    public static void startPhoneNumberValidation(Activity activity) {
        Intent intent = new Intent(activity, GooglePhoneNumberValidation.class);
        activity.startActivityForResult(intent, REQ_CODE_G_PHONE_VALIDATION);
    }

    public static String onActivityResults(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_G_PHONE_VALIDATION && resultCode == Activity.RESULT_OK) {
            return data.getStringExtra(Param.param0);
        } else {
            return "";
        }
    }

    public static Boolean onActivityResults(int requestCode, int resultCode, Intent data, Boolean isVerify) {
        return requestCode == REQ_CODE_G_PHONE_VALIDATION && resultCode == Activity.RESULT_OK;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_google_phoneno_validation);

        initComponent();
        initContent();
        initListener();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void initComponent() {
        ccpValidPhoneNmbrfvbi = (CountryCodePicker) findViewById(R.id.ccpValidPhoneNmbr);
        tvValidPhoneNmbrInfofvbi = (TextView) findViewById(R.id.tvValidPhoneNmbrInfo);
        etValidPhoneNmbrfvbi = (EditText) findViewById(R.id.etValidPhoneNmbr);
        llValidPhoneNmbrSubmitfvbi = (LinearLayout) findViewById(R.id.llValidPhoneNmbrSubmit);
        tvValidPhoneNmbrSubmitfvbi = (TextView) findViewById(R.id.tvValidPhoneNmbrSubmit);
        tvLoginResendCodefvbi = (TextView) findViewById(R.id.tvValidPhoneNmbrResendCode);
        llValidPhoneNmbrBackfvbi = (LinearLayout) findViewById(R.id.llValidPhoneNmbrBack);
        tvValidPhoneNmbrAppVersionfvbi = (TextView) findViewById(R.id.tvValidPhoneNmbrAppVersion);
    }

    private void initContent() {
        mAuth = FirebaseAuth.getInstance();

        updateUI(UI_LOGIN, null);

        //getversion
        try {
            appVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            appVersionCode = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appName = getResources().getString(R.string.app_name);
        appPkg = getBaseContext().getPackageName();

        tvValidPhoneNmbrAppVersionfvbi.setText(getString(R.string.app_name) + " - v " + appVersionName + "." + appVersionCode);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @SuppressLint("LongLogTag")
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     USER action.

                signInWithPhoneAuthCredential(credential);//onVerificationCompleted

                if (credential != null) {
                    if (credential.getSmsCode() != null) {
                        updateUI(UI_VERIFY, credential);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                //code here
                                etValidPhoneNmbrfvbi.setText(credential.getSmsCode());
                            }
                        }, 1000L);
                    } else {
                        snackBar("instant validation", false);
                    }
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                //Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    etValidPhoneNmbrfvbi.setError("Invalid phone number, please check your number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    snackBar("Quota exceeded.", false); // The SMS quota for the project has been exceeded
                } else {
                    snackBar(e.getMessage(), false);
                }

                updateUI(UI_LOGIN, null);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // Update UI
                updateUI(UI_VERIFY, null);
            }
        };
    }

    private void initListener() {
        etValidPhoneNmbrfvbi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_GO) {
                    checkData();
                }
                return handled;
            }
        });

        llValidPhoneNmbrSubmitfvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

        tvLoginResendCodefvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phoneNumberSavedwPlus, mResendToken);
            }
        });

        llValidPhoneNmbrBackfvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

            }
        });
    }

    private void updateUI(int ui, PhoneAuthCredential cred) {
        if (ui == UI_LOGIN) {
            hideViews(tvValidPhoneNmbrInfofvbi, tvLoginResendCodefvbi, llValidPhoneNmbrBackfvbi);
            showViews(ccpValidPhoneNmbrfvbi);
            etValidPhoneNmbrfvbi.setHint("Enter Phone Number");
            tvValidPhoneNmbrSubmitfvbi.setText("VERIFY");
            isLogin = true;
            isVerify = false;
        } else if (ui == UI_VERIFY) {
            showViews(tvValidPhoneNmbrInfofvbi, tvLoginResendCodefvbi, llValidPhoneNmbrBackfvbi);
            hideViews(ccpValidPhoneNmbrfvbi);
            etValidPhoneNmbrfvbi.setHint("Enter Code");
            etValidPhoneNmbrfvbi.setText("");
            tvValidPhoneNmbrSubmitfvbi.setText("SEND");
            isLogin = false;
            isVerify = true;

            /*if (cred != null) {
                if (cred.getSmsCode() != null) {
                    etValidPhoneNmbrfvbi.setText(cred.getSmsCode());
                } else {
                    snackBar("instant validation", false);
                }
            }*/
        }
    }

    private void checkData() {
        if (!isEmptyField(etValidPhoneNmbrfvbi)) {
            if (isLogin) {
                if (isValidatePhoneNumber(etValidPhoneNmbrfvbi.getText().toString())) {
                    //Toast.makeText(getApplicationContext(), getNumberValid(etValidPhoneNmbrfvbi), Toast.LENGTH_SHORT).show();
                    startPhoneNumberVerification("+" + getNumberValid(etValidPhoneNmbrfvbi));
                    phoneNumberSavedwPlus = "+" + getNumberValid(etValidPhoneNmbrfvbi);
                    phoneNumberSavedwoPlus = getNumberValid(etValidPhoneNmbrfvbi);
                } else {
                    etReqFocus("Data Tidak Valid", etValidPhoneNmbrfvbi);
                }
            } else if (isVerify) {
                verifyPhoneNumberWithCode(mVerificationId, etValidPhoneNmbrfvbi.getText().toString());
            }
        } else {
            etReqFocus("Data Kosong", etValidPhoneNmbrfvbi);
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                timeoutDuration,    // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                timeoutDuration,    // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);//verifyPhoneNumberWithCode
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    snackBar("Success", false);
                    backToFinish();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        snackBar("Invalid code.", true);
                    }
                }
            }
        });
    }

    private void backToFinish() {
        Intent intent = new Intent();
        intent.putExtra(Param.param0, phoneNumberSavedwoPlus);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setEditTextMaxLength(int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        etValidPhoneNmbrfvbi.setFilters(filterArray);
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(UI_LOGIN, null);
        Snackbar.make(findViewById(android.R.id.content), "Success Sign Out", Snackbar.LENGTH_SHORT).show();
    }

    private void showViews(View... views) {
        for (View v : views) {
            //v.setEnabled(true);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void hideViews(View... views) {
        for (View v : views) {
            //v.setEnabled(false);
            v.setVisibility(View.GONE);
        }
    }

    private void snackBar(String success, Boolean reloadAction) {
        if (reloadAction) {
            Snackbar.make(findViewById(android.R.id.content), success, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RELOAD", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkData();
                        }
                    })
                    .show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), success, Snackbar.LENGTH_LONG).show();
        }
    }

    private String getNumberValid(EditText editText) {
        String string = editText.getText().toString();
        String sub0 = string.substring(0, 1);
        String sub62 = string.substring(0, 2);
        String subPlus62 = string.substring(0, 3);
        String sVal = null;
        if (sub0.equalsIgnoreCase("0")) {
            sVal = ccpValidPhoneNmbrfvbi.getSelectedCountryCode() + string.substring(1);
        } else if (sub62.equalsIgnoreCase(ccpValidPhoneNmbrfvbi.getSelectedCountryCode())) {
            sVal = ccpValidPhoneNmbrfvbi.getSelectedCountryCode() + string.substring(2);
        } else if (subPlus62.equalsIgnoreCase(ccpValidPhoneNmbrfvbi.getSelectedCountryCodeWithPlus())) {
            sVal = ccpValidPhoneNmbrfvbi.getSelectedCountryCode() + string.substring(3);
        } else {
            sVal = ccpValidPhoneNmbrfvbi.getSelectedCountryCode() + string;
        }
        return sVal;
    }

    private void etReqFocus(String s, TextView editText) {
        editText.setError(s);
        editText.requestFocus();
    }

    private boolean isEmptyField(EditText editText) {
        String s1 = editText.getText().toString();
        return TextUtils.isEmpty(s1);
    }

    private boolean isValidatePhoneNumber(CharSequence num) {
        String PHONE_NUMBER_PATTERN = "^[+]?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    public interface Param {
        String param0 = "kanzankazu";
    }

}
