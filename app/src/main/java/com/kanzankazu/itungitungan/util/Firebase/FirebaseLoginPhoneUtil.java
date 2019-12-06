package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

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
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.model.User;

import java.util.concurrent.TimeUnit;

public class FirebaseLoginPhoneUtil {
    private static final String TAG = "LoginPhoneUtil";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private final Activity activity;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private FirebaseLoginUtil.FirebaseLoginListener.Phone mListenerPhone;
    private final FirebaseAuth mAuth;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;

    public FirebaseLoginPhoneUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Phone mListenerPhone) {
        this.activity = activity;
        this.mListener = mListener;
        this.mListenerPhone = mListenerPhone;

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     USER action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;

                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential, "");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
                String s = "";

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    //mPhoneNumberField.setError("Invalid phone number.");
                    s = "Invalid phone number.";
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    //Snackbar.make(activity.findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                    s = "Quota exceeded.";
                }

                // Show a message and update the UI
                updateUI(s);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the USER to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // Update UI
                updateUI(STATE_CODE_SENT);
            }
        };
    }

    public void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }

    public void startPhoneNumberCode(String verificationId, String code) {
        if (!TextUtils.isEmpty(verificationId) && !TextUtils.isEmpty(code)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            mListenerPhone.uiPhoneCodeNotSent();
        }
    }

    public void startResendPhoneNumberCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    /**
     * call onStart
     */
    public void isSignIn() {
        // Check if USER is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser, "");

    }

    private boolean validatePhoneNumber(EditText editText) {
        String phoneNumber = editText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            //mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in USER's information
                    Log.d(TAG, "signInWithCredential:success");

                    FirebaseUser user = task.getResult().getUser();

                    updateUI(STATE_SIGNIN_SUCCESS, user, "");

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    String s = "";
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        //mVerificationField.setError("Invalid code.");
                        s = "Invalid code.";
                    }
                    // Update UI
                    updateUI(STATE_SIGNIN_FAILED, s);
                }
            }
        });
    }

    private void updateUI(String message) {
        updateUI(0, message);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, "");
    }

    private void updateUI(FirebaseUser user, String message) {
        if (user == null) {
            updateUI(STATE_INITIALIZED, message);
        } else {
            updateUI(STATE_SIGNIN_SUCCESS, user, message);
        }
    }

    private void updateUI(int uiState, String message) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(int uiState, FirebaseUser user, String message) {
        updateUI(uiState, user, null, message);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred, String message) {
        updateUI(uiState, null, cred, message);
    }

    private void updateUI(int uiState, FirebaseUser firebaseUser, PhoneAuthCredential cred, String message) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                //enableViews(mStartButton, mPhoneNumberField);
                //disableViews(mVerifyButton, mResendButton, mVerificationField);
                //mDetailText.setText(null);
                mListenerPhone.uiPhoneInitialize();
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                //enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //disableViews(mStartButton);
                //mDetailText.setText(R.string.status_code_sent);
                mListenerPhone.uiPhoneCodeSent();
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                //enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //mDetailText.setText(R.string.status_verification_failed);
                mListenerPhone.uiPhoneVerifyFailed(message);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                //disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //mDetailText.setText(R.string.status_verification_succeeded);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        // mVerificationField.setText(cred.getSmsCode());
                        mListenerPhone.uiPhoneVerifySuccess(cred.getSmsCode());
                    } else {
                        //mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                //mDetailText.setText(R.string.status_sign_in_failed);
                mListenerPhone.uiPhoneSignInFailed(message);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
        }

        if (firebaseUser != null) {
            // Signed in
            //mPhoneNumberViews.setVisibility(View.GONE);
            //mSignedInViews.setVisibility(View.VISIBLE);

            //enableViews(mPhoneNumberField, mVerificationField);
            //mPhoneNumberField.setText(null);
            //mVerificationField.setText(null);

            //mStatusText.setText(R.string.signed_in);
            //mDetailText.setText(getString(R.string.firebase_status_fmt, USER.getUid()));

            mListener.uiSignInSuccess(firebaseUser);

        } else {
            // Signed out
            //mPhoneNumberViews.setVisibility(View.VISIBLE);
            //mSignedInViews.setVisibility(View.GONE);

            //mStatusText.setText(R.string.signed_out);

            mListener.uiSignInFailed(activity.getString(R.string.message_signin_failed));
        }
    }
}
