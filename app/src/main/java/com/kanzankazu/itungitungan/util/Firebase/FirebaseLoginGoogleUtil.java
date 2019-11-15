package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kanzankazu.itungitungan.R;

public class FirebaseLoginGoogleUtil extends FirebaseConnectionUtil {
    public static final int RC_SIGN_IN = 120;
    public static final String TAG = "LoginGoogleUtil";

    private final GoogleSignInClient mGoogleSignInClient;
    private final GoogleApiClient mGoogleApiClient;
    private final Activity activity;
    private final FirebaseAuth mAuth;
    private FirebaseLoginUtil.FirebaseLoginListener mListener;
    private FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle;

    public FirebaseLoginGoogleUtil(Activity activity, FirebaseLoginUtil.FirebaseLoginListener mListener, FirebaseLoginUtil.FirebaseLoginListener.Google mListenerGoogle) {
        this.activity = activity;
        this.mListener = mListener;
        this.mListenerGoogle = mListenerGoogle;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * call startActivityForResult
     */
    public void signIn() {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * @return add in startActivityResult
     */
    public Intent signInFromFragment() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
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
                mListener.loginProgressDismiss();
                if (task.isSuccessful()) {
                    mListenerGoogle.uiRevokeGoogleSuccess();
                } else {
                    mListenerGoogle.uiRevokeGoogleUnSuccess();
                }
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
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mListener.loginProgressDismiss();
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
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getId());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getIdToken());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getDisplayName());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getEmail());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getGivenName());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getFamilyName());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getPhotoUrl());
        Log.d("Lihat", "firebaseAuthWithGoogle FirebaseLoginGoogleUtil : " + acct.getServerAuthCode());

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
                    mListenerGoogle.uiSignInGoogleFailed("");
                    mListener.loginProgressDismiss();
                }
            }
        });
    }

    public boolean isSignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    public FirebaseUser getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            return user;
        }
        return null;
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
