package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.util.NetworkUtil;

public class FirebaseLoginEmailPasswordUtil {

    private static final String TAG = "LoginEmailPasswordUtil";

    private final Activity mActivity;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private FirebaseLoginUtil.FirebaseLoginListener.EmailPass mListenerEmailPass;
    private final FirebaseAuth mAuth;

    public FirebaseLoginEmailPasswordUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener,FirebaseLoginUtil.FirebaseLoginListener.EmailPass mListenerEmailPass) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mListenerEmailPass = mListenerEmailPass;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    /**
     * call onStart
     */
    public void isSignIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public boolean createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) {
            return false;
        }

        mListener.loginProgressShow();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mListener.loginProgressDismiss();
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    Toast.makeText(mActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

        return true;
    }

    public boolean signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            return false;
        }

        mListener.loginProgressShow();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                    Toast.makeText(mActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                mListener.loginProgressDismiss();
            }
        });
        // [END sign_in_with_email]

        return true;
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void sendEmailVerification() {
        // Disable button
        mListenerEmailPass.uiDisableEmailPassSubmitButton();

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Re-enable button
                mListenerEmailPass.uiEnableEmailPassSubmitButton();

                if (task.isSuccessful()) {
                    Toast.makeText(mActivity, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(mActivity, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            valid = false;
        }

        return valid;
    }

    private boolean validateForm(EditText mEmailField, EditText mPasswordField) {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        mListener.loginProgressDismiss();
        if (user != null) {
            mListener.uiSignInSuccess(user);
        } else {
            mListener.uiSignOutSuccess();
        }
    }

    public interface FirebaseLoginEmailPasswordListener {
        void uiSignInSuccess(FirebaseUser user);

        void uiSignOutSuccess();

        void uiDisableButton();

        void uiDEnableButton();

        void loginProgressShow();

        void loginProgressDismiss();
    }
}
