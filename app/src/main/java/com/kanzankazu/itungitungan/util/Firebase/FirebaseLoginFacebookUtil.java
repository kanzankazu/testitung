package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
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

public class FirebaseLoginFacebookUtil {
    private static final String TAG = "LoginGoogleUtil";

    private final Activity activity;
    private final FirebaseLoginUtil.FirebaseLoginListener mListener;
    private final FirebaseAuth mAuth;
    private final CallbackManager mCallbackManager;

    public FirebaseLoginFacebookUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener, LoginButton loginButton) {
        this.activity = activity;
        this.mListener = mListener;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                updateUI(null);
            }
        });
    }

    /**
     * call onStart
     */
    public void isSignIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        updateUI(null);
    }

    /**
     * call in onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void signInActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mListener.loginProgressShow();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                mListener.loginProgressDismiss();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        mListener.loginProgressDismiss();
        if (user != null) {
            mListener.uiSignInSuccess(user);
        } else {
            mListener.uiSignOutSuccess();
        }
    }

    public interface FirebaseLoginFacebookListener {
        void uiSignInSuccess(FirebaseUser user);

        void uiSignOutSuccess();

        void loginProgressShow();

        void loginProgressDismiss();
    }
}
