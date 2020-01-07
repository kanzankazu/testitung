package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.kanzankazu.itungitungan.view.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faisal Bahri on 2020-01-07.
 */
public class MyFirebaseHandlerDirect {

    static final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    static final private String serverKey = "key=" + "Your Firebase server key";
    static final private String contentType = "application/json";
    static final String TAG = "NOTIFICATION TAG";

    static String NOTIFICATION_TITLE;
    static String NOTIFICATION_MESSAGE;
    static String TOPIC;

    public static void makeNotification(Activity activity, String title, String message) {
        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE = message;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
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
                    Snackbar.make(activity.findViewById(android.R.id.content), response.toString(), Snackbar.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(activity, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
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
