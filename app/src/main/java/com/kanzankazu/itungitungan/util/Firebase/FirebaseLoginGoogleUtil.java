package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kanzankazu.itungitungan.R;

public class FirebaseLoginGoogleUtil extends FirebaseLoginUtil implements FirebaseConnectionUtil.FirebaseConnectionListener {

    public static final int RC_SIGN_IN = 120;
    private static final String TAG = "LoginGoogleUtil";
    private final GoogleSignInClient mGoogleSignInClient;
    private final GoogleApiClient mGoogleApiClient;
    private FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle;

    public FirebaseLoginGoogleUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle) {
        super(mActivity, mListener);
        this.mListenerGoogle = mListenerGoogle;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        mGoogleApiClient.clearDefaultAccountAndReconnect(); // To remove to previously selected user's account so that the choose account UI will show
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * call startActivityForResult
     *
     */
    public void signIn() {
        if (isConnected(mActivity, this)) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            } else {
                mGoogleApiClient.connect();
            }

            mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    /**
     * @return add in startActivityResult
     */
    public Intent signInFromFragment() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void revokeAccess() {
        if (isConnected(mActivity, this)) {
            // Firebase sign out
            signOut(false);

            // Google revoke access
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(mActivity, task -> {
                if (task.isSuccessful()) {
                    mListenerGoogle.uiRevokeGoogleSuccess(mActivity.getString(R.string.message_revoke_success));
                } else {
                    mListenerGoogle.uiRevokeGoogleFailed(task.getException().getMessage());
                }
            });
        }
    }

    /**
     * call in onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void signInActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && isConnected(mActivity, this)) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } else {
                    Log.d("Lihat", "signInActivityResult FirebaseLoginGoogleUtil : " + task.getException().getMessage());
                    mListenerGoogle.uiSignInGoogleFailed(task.toString());
                }
            } catch (ApiException e) {
                Log.d("Lihat", "signInActivityResult FirebaseLoginGoogleUtil : " + e.toString());
                mListenerGoogle.uiSignInGoogleFailure(checkException(e.getStatusCode()));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (isConnected(mActivity,this)){
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getId());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getIdToken());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getDisplayName());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getEmail());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getGivenName());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getFamilyName());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getPhotoUrl());
            Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getServerAuthCode());

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            mListener.uiSignInSuccess(firebaseUser);
                        } else {
                            mListener.uiSignInFailed(task.getException().getMessage());
                        }
                    });
        }
    }
}
