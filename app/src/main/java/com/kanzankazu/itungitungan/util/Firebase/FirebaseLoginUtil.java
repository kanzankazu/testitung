package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.model.User;

import java.util.List;

public class FirebaseLoginUtil extends FirebaseConnectionUtil {

    public String TAG = "LoginUtil";
    public Activity mActivity;
    public FirebaseAuth mAuth;
    public FirebaseLoginListener mListener;

    public FirebaseLoginUtil(Activity mActivity, FirebaseLoginUtil.FirebaseLoginListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isSignIn() {
        // Check if USER is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public FirebaseUser getUserProfile() {
        return mAuth.getCurrentUser();
    }

    /**
     * @return null/
     */
    public List<? extends UserInfo> getProviderData() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            for (UserInfo profile : firebaseUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
            return firebaseUser.getProviderData();
        }
        return null;
    }

    public void updateProfile(String name, String path) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(path))
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    } else {
                        Log.d(TAG, "User profile failed.");
                    }
                });
    }

    public void updateEmail(String email) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseUser.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                    } else {
                        Log.d(TAG, "User email address failed.");
                    }
                });
    }

    public void updatePassword(String password) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseUser.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User password updated.");
                    } else {
                        Log.d(TAG, "User password failed.");
                    }
                });
    }

    public void sendEmailVerification() {
        FirebaseAuth auth = mAuth;
        FirebaseUser firebaseUser = auth.getCurrentUser();

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    } else {
                        Log.d(TAG, "Email not sent.");
                    }
                });
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

    public boolean isEmailVerfied(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            return firebaseUser.isEmailVerified();
        } else {
            return false;
        }
    }

    public void sendPasswordReset(String email) {
        FirebaseAuth auth = mAuth;

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    } else {
                        Log.d(TAG, "Email not sent.");
                    }
                });
    }

    public void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                        mListener.uiSignOutSuccess();
                    } else {
                        Log.d(TAG, "User account failed deleted.");
                    }
                });
    }

    public void reauthenticate(String email, String password) {
        FirebaseUser user = mAuth.getCurrentUser();

        // Get auth credentials from the USER for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the USER to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User re-authenticated success.");
                    } else {
                        Log.d(TAG, "User re-authenticated failed.");

                    }
                });
    }

    public void authWithGithub(Activity activity) {

        String token = "<GITHUB-ACCESS-TOKEN>";
        AuthCredential credential = GithubAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the USER. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in USER can be handled in the listener.
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Authentication Success.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "signInWithCredential", task.getException());
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void linkAndMerge(AuthCredential credential) {

        FirebaseUser prevUser = mAuth.getCurrentUser();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    FirebaseUser currentUser = task.getResult().getUser();
                    // Merge prevUser and currentUser accounts and data
                    // ...
                });
    }

    public void unlink(Activity activity, String providerId) {

        mAuth.getCurrentUser().unlink(providerId)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Auth provider unlinked from account
                        // ...
                    }
                });
    }

    public void buildActionCodeSettings() {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                //.setUrl("https://www.example.com/finishSignUp?cartId=1234")
                // This must be true
                .setHandleCodeInApp(true)
                //.setIOSBundleId("com.example.ios")
                .setAndroidPackageName(
                        "com.example.android",
                        true, /* installIfNotAvailable */
                        "12"    /* minimumVersion */)
                .build();
    }

    public void sendSignInLink(String email, ActionCodeSettings actionCodeSettings) {
        FirebaseAuth auth = mAuth;
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    } else {
                        Log.d(TAG, "Email not sent.");
                    }
                });
    }

    public void verifySignInLink(Activity activity) {
        FirebaseAuth auth = mAuth;
        Intent intent = activity.getIntent();
        String emailLink = intent.getData().toString();

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            String email = "someemail@domain.com";

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully signed in with email link!");
                            AuthResult result = task.getResult();
                            // You can access the new USER via result.getUser()
                            // Additional USER info profile *not* available via:
                            // result.getAdditionalUserInfo().getProfile() == null
                            // You can check if the USER is new or existing:
                            // result.getAdditionalUserInfo().isNewUser()
                        } else {
                            Log.e(TAG, "Error signing in with email link", task.getException());
                        }
                    });
        }
    }

    public void linkWithSignInLink(String email, String emailLink) {
        FirebaseAuth auth = mAuth;

        // Construct the email link credential from the current URL.
        AuthCredential credential = EmailAuthProvider.getCredentialWithLink(email, emailLink);

        // Link the credential to the current USER.
        auth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully linked emailLink credential!");
                        AuthResult result = task.getResult();
                        // You can access the new USER via result.getUser()
                        // Additional USER info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the USER is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e(TAG, "Error linking emailLink credential", task.getException());
                    }
                });
    }

    public void reauthWithLink(String email, String emailLink) {
        FirebaseAuth auth = mAuth;

        // Construct the email link credential from the current URL.
        AuthCredential credential = EmailAuthProvider.getCredentialWithLink(email, emailLink);

        // Re-authenticate the USER with this credential.
        auth.getCurrentUser().reauthenticateAndRetrieveData(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User is now successfully reauthenticated
                    } else {
                        Log.e(TAG, "Error reauthenticating", task.getException());
                    }
                });
    }

    public void differentiateLink(String email) {
        FirebaseAuth auth = mAuth;

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // User can sign in with email/password
                            } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                // User can sign in with email/link
                            }
                        } else {
                            Log.e(TAG, "Error getting sign in methods for USER", task.getException());
                        }
                    }
                });
        // [END auth_differentiate_link]
    }

    public void getGoogleCredentials() {
        String googleIdToken = "";
        // [START auth_google_cred]
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        // [END auth_google_cred]
    }

    public void getFbCredentials() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        // [START auth_fb_cred]
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        // [END auth_fb_cred]
    }

    public void getEmailCredentials() {
        String email = "";
        String password = "";
        // [START auth_email_cred]
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        // [END auth_email_cred]
    }

    public void signOut() {
        mAuth.signOut();
        mListener.uiSignOutSuccess();
    }

    public interface FirebaseLoginListener {

        void uiSignInSuccess(User user);

        void uiSignInFailed(String errorMessage);

        void uiSignOutSuccess();

        void uiConnectionError(String messageError, String typeError);

        interface EmailPass {
            /*EMAILPASS*/
            void uiSignUpSuccess(User user);

            void uiSignUpFailed(String errorMessage);

            void uiDisableEmailPassSubmitButton();

            void uiEnableEmailPassSubmitButton();

            void uiEmailVerifySentSuccess(FirebaseUser firebaseUser);

            void uiEmailVerifySentFailed();
        }

        interface Google {
            /*GOOGLE*/
            void uiSignInGoogleFailure(String messageError);

            void uiSignInGoogleFailed(String messageError);

            void uiRevokeGoogleSuccess(String string);

            void uiRevokeGoogleFailed(String message);
        }

        interface Phone {
            /*PHONE*/
            void uiPhoneInitialize();

            void uiPhoneCodeSent();

            void uiPhoneCodeNotSent();

            void uiPhoneVerifyFailed(String messageError);

            void uiPhoneVerifySuccess(String smsCode);

            void uiPhoneSignInFailed(String messageError);
        }
    }
}
