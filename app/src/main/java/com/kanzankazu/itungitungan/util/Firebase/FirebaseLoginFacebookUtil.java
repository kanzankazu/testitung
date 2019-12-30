package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kanzankazu.itungitungan.R;

public class FirebaseLoginFacebookUtil extends FirebaseLoginUtil {
    private static final String TAG = "LoginGoogleUtil";

    private final FirebaseAuth mAuth;
    private final CallbackManager mCallbackManager;
    private final FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle;

    public FirebaseLoginFacebookUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle) {
        super(mActivity, mListener);
        this.mListenerGoogle = mListenerGoogle;

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseLoginFacebookUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle, LoginButton loginButton) {
        super(mActivity, mListener);
        this.mListenerGoogle = mListenerGoogle;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (isLoggedIn) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                } /*else {
                    mListener.uiSignOutSuccess(isRefresh);
                }*/
            }

            @Override
            public void onCancel() {
                mListenerGoogle.uiSignInGoogleFailed(mActivity.getString(R.string.message_signin_failed));
            }

            @Override
            public void onError(FacebookException error) {
                mListenerGoogle.uiSignInGoogleFailure(error.getMessage());
            }
        });
    }

    public void signIn() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                        if (isLoggedIn) {
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        } /*else {
                            mListener.uiSignOutSuccess(isRefresh);
                        }*/

                    }

                    @Override
                    public void onCancel() {
                        mListenerGoogle.uiSignInGoogleFailed(mActivity.getString(R.string.message_signin_failed));
                    }

                    @Override
                    public void onError(FacebookException exception) {
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
        // Pass the mActivity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    mListener.uiSignInSuccess(firebaseUser);
                } else {
                    mListenerGoogle.uiSignInGoogleFailed(task.getException().getMessage());
                }
            }
        });
    }

    public void accessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                handleFacebookAccessToken(currentAccessToken);
            }
        };
        // If the access token is available already assign it.
        //accessToken = AccessToken.getCurrentAccessToken();
    }
}
