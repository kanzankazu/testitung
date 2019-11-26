package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.model.User;

public class FirebaseLoginEmailPasswordUtil {

    private static final String TAG = "LoginEmailPasswordUtil";

    private final Activity mActivity;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private final FirebaseAuth mAuth;
    private FirebaseLoginUtil.FirebaseLoginListener.EmailPass mListenerEmailPass;

    public FirebaseLoginEmailPasswordUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.EmailPass mListenerEmailPass) {
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
        // Check if USER is signed in (non-null) and update UI accordingly.
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mListener.uiSignInSuccess(new User(firebaseUser));
    }

    public boolean createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    mListener.loginProgressDismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        mListener.uiSignInSuccess(new User(firebaseUser));
                    } else {
                        mListener.uiSignInFailed(task.getException().getMessage());
                    }
                });

        return true;
    }

    public boolean signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mActivity, task -> {
            mListener.loginProgressDismiss();

            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                mListener.uiSignInSuccess(new User(user));
            } else {
                mListener.uiSignInFailed(task.getException().getMessage());
            }

        });

        return true;
    }

    public void signOut() {
        mAuth.signOut();
        mListener.uiSignOutSuccess();
    }

    public void sendEmailVerification() {
        // Disable button
        mListenerEmailPass.uiDisableEmailPassSubmitButton();

        // Send verification email
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(mActivity, task -> {
                    // Re-enable button
                    mListenerEmailPass.uiEnableEmailPassSubmitButton();

                    if (task.isSuccessful()) {
                        mListenerEmailPass.uiEmailVerifySentSuccess(firebaseUser);
                    } else {
                        mListenerEmailPass.uiEmailVerifySentFailed();
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
}
