package com.kanzankazu.itungitungan.util.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

/**
 * Created by Faisal Bahri on 2019-12-04.
 */
public class FirebaseMessagingHandler {
    public void enableFCM(){
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    public void disableFCM(){
        // Disable auto init
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            try {
                // Remove InstanceID initiate to unsubscribe all topic
                // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
