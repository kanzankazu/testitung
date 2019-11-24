package com.kanzankazu.itungitungan.util.Facebook;

import android.app.Activity;
import android.content.Intent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.model.User;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil;

import java.util.concurrent.Executor;

public class FacebookLoginUtil {
    private final CallbackManager callbackManager;
    private final FirebaseAuth mAuth;
    private Activity mActivity;
    private FirebaseLoginUtil.FirebaseLoginListener mListener;
    private FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle;

    public FacebookLoginUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mListenerGoogle = mListenerGoogle;

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }

    public void signIn() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                        if (isLoggedIn){
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }else {
                            mListener.uiSignOutSuccess();
                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        mListenerGoogle.uiSignInGoogleFailed(mActivity.getString(R.string.message_login_failed));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        mListenerGoogle.uiSignInGoogleFailure(exception.getMessage());
                    }
                });
    }

    /**
     * call in onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void signInActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        mListener.uiSignInSuccess(new User(user));
                    } else {
                        mListenerGoogle.uiSignInGoogleFailed(task.getException().getMessage());
                    }
                });
    }

}
