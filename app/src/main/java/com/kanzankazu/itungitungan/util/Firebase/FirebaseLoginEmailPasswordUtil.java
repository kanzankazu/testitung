package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.model.User;

public class FirebaseLoginEmailPasswordUtil extends FirebaseLoginUtil implements FirebaseConnectionUtil.FirebaseConnectionListener {

    private final FirebaseLoginListener.EmailPass mListenerEmailPass;

    public FirebaseLoginEmailPasswordUtil(Activity activity, FirebaseLoginListener mListener, FirebaseLoginListener.EmailPass mListenerEmailPass) {
        super(activity, mListener);
        this.mListenerEmailPass = mListenerEmailPass;
    }

    public void createAccount(String email, String password) {
        if (isConnected(mActivity, this)) mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        mListenerEmailPass.uiSignUpSuccess(new User(firebaseUser));
                    } else {
                        mListenerEmailPass.uiSignUpFailed(task.getException().getMessage());
                    }
                });
    }

    public void signIn(String email, String password) {
        if (isConnected(mActivity, this)) mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        mListener.uiSignInSuccess(firebaseUser);
                    } else {
                        mListener.uiSignInFailed(task.getException().getMessage());
                    }
                });
    }

    public void sendEmailVerification() {
        if (isConnected(mActivity,this)){
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

    public void sendEmailVerificationWithContinueUrl() {
        FirebaseAuth auth = mAuth;
        FirebaseUser firebaseUser = auth.getCurrentUser();

        String url = "http://www.example.com/verify?uid=" + firebaseUser.getUid();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.example.ios")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName(BuildConfig.APPLICATION_ID, false, null)
                .build();

        firebaseUser.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    } else {

                    }
                });

        // auth.setLanguageCode("fr");
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage();
    }
}
