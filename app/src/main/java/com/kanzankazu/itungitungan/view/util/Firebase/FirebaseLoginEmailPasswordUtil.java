package com.kanzankazu.itungitungan.view.util.Firebase;

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

public class FirebaseLoginEmailPasswordUtil {

    private static final String TAG = "LoginEmailPasswordUtil";

    private final Activity activity;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private final FirebaseAuth mAuth;

    public FirebaseLoginEmailPasswordUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener) {
        this.activity = activity;
        this.mListener = mListener;

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

    public void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mListener.loginProgressShow();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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

                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mListener.loginProgressShow();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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

                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                if (!task.isSuccessful()) {
                    //mStatusTextView.setText(R.string.auth_failed);
                }
                mListener.loginProgressDismiss();
            }
        });
        // [END sign_in_with_email]
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void sendEmailVerification() {
        // Disable button
        mListener.uiDisableButton();

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Re-enable button
                mListener.uiDEnableButton();

                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(activity, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
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
