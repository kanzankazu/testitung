package com.kanzankazu.itungitungan.view.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kanzankazu.itungitungan.BuildConfig;

public class FirebaseLoginGoogleUtil {
    private static final int RC_SIGN_IN = 120;
    private static final String TAG = "LoginGoogleUtil";

    private final GoogleSignInClient mGoogleSignInClient;
    private final Activity activity;
    private FirebaseLoginUtil.FirebaseLoginListener mListener;
    private final FirebaseAuth mAuth;

    public FirebaseLoginGoogleUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener) {
        this.activity = activity;
        this.mListener = mListener;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_AUTH_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * call startActivityForResult
     */
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //updateUI(null);
                mListener.uiSignOutSuccess();
                mListener.loginProgressDismiss();
            }
        });
    }

    public void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //updateUI(null);
                mListener.uiRevokeSuccess();
                mListener.loginProgressDismiss();
            }
        });

    }

    /**
     * call in onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void signInActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

                //updateUI(null);
                mListener.uiSignInFailure(e.toString());
                mListener.loginProgressDismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //showProgressDialog();
        mListener.loginProgressShow();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    mListener.uiSignInSuccess(user);
                    mListener.loginProgressDismiss();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Snackbar.make(activity.findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    //updateUI(null);
                    mListener.uiSignInFailed("");
                    mListener.loginProgressDismiss();
                }
            }
        });
    }

    public interface FirebaseLoginGoogleListener {
        void uiSignInSuccess(FirebaseUser user);

        void uiSignInFailed(FirebaseUser user);

        void uiSignInFailure(FirebaseUser user);

        void uiSignOutSuccess(FirebaseUser user);

        void uiRevokeSuccess(FirebaseUser user);

        void loginProgressShow();

        void loginProgressDismiss();
    }
}
