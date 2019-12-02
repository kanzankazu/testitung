package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.model.User;

public class FirebaseLoginEmailPasswordUtil extends FirebaseLoginUtil {

    private final FirebaseLoginListener.EmailPass mListenerEmailPass;

    public FirebaseLoginEmailPasswordUtil(Activity activity, FirebaseLoginListener mListener, FirebaseLoginListener.EmailPass mListenerEmailPass) {
        super(activity, mListener);
        this.mListenerEmailPass = mListenerEmailPass;
    }

    public boolean createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        mListenerEmailPass.uiSignUpSuccess(new User(firebaseUser));
                    } else {
                        mListenerEmailPass.uiSignUpFailed(task.getException().getMessage());
                    }
                });

        return true;
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        mListener.uiSignInSuccess(new User(firebaseUser));
                    } else {
                        mListener.uiSignInFailed(task.getException().getMessage());
                    }
                });
    }

    public void sendEmailVerification() {
        mListenerEmailPass.uiDisableEmailPassSubmitButton();

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
}
