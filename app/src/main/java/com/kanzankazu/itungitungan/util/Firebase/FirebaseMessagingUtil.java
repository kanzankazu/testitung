package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.view.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faisal Bahri on 2020-01-07.
 */
public class FirebaseMessagingUtil {

    static final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    static final private String serverKey = "key=" + BuildConfig.FIREBASE_SERVER_KEY;
    static final private String contentType = "application/json";
    private static final String TAG = "NOTIFICATION TAG";

    public static void makeNotificationTopic(Activity activity, String topics, String title, String message, String type, String id, String idSub) {
        String TOPIC = "/topics/" + topics; //topic has to match what the receiver subscribed to

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put(Constants.FirebasePushNotif.title, title);
            notifcationBody.put(Constants.FirebasePushNotif.message, message);
            notifcationBody.put(Constants.FirebasePushNotif.type, type);
            notifcationBody.put(Constants.FirebasePushNotif.id, id);
            notifcationBody.put(Constants.FirebasePushNotif.idSub, idSub);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(activity, notification);
    }

    public static void makeNotificationToken(Activity activity, String token, String title, String message, String type, String id, String idSub) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put(Constants.FirebasePushNotif.title, title);
            notifcationBody.put(Constants.FirebasePushNotif.message, message);
            notifcationBody.put(Constants.FirebasePushNotif.type, type);
            notifcationBody.put(Constants.FirebasePushNotif.id, id);
            notifcationBody.put(Constants.FirebasePushNotif.idSub, idSub);

            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(activity, notification);
    }

    public static void sendNotification(Activity activity, JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
                    //Snackbar.make(activity.findViewById(android.R.id.content), response.toString(), Snackbar.LENGTH_SHORT).show();
                },
                error -> {
                    Log.i(TAG, "onErrorResponse: Didn't work");
                    Toast.makeText(activity, "Request error", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }
}
