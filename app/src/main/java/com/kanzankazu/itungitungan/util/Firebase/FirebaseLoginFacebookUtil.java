package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.model.User;

public class FirebaseLoginFacebookUtil {
    private static final String TAG = "LoginGoogleUtil";

    private final Activity mActivity;
    private final FirebaseAuth mAuth;
    private final CallbackManager mCallbackManager;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private final FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle;

    public FirebaseLoginFacebookUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mListenerGoogle = mListenerGoogle;

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseLoginFacebookUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle, LoginButton loginButton) {
        this.mActivity = activity;
        this.mListener = mListener;
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
                } else {
                    mListener.uiSignOutSuccess();
                }
            }

            @Override
            public void onCancel() {
                mListenerGoogle.uiSignInGoogleFailed(mActivity.getString(R.string.message_login_failed));
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
                        } else {
                            mListener.uiSignOutSuccess();
                        }

                    }

                    @Override
                    public void onCancel() {
                        mListenerGoogle.uiSignInGoogleFailed(mActivity.getString(R.string.message_login_failed));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        mListenerGoogle.uiSignInGoogleFailure(exception.getMessage());
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        mListener.uiSignOutSuccess();
    }

    /**
     * call onStart
     */
    public void isSignIn() {
        // Check if USER is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mListener.uiSignInSuccess(new User(currentUser));
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
                    FirebaseUser user = mAuth.getCurrentUser();
                    mListener.uiSignInSuccess(new User(user));
                } else {
                    mListenerGoogle.uiSignInGoogleFailed(task.getException().getMessage());
                }

                mListener.loginProgressDismiss();
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
