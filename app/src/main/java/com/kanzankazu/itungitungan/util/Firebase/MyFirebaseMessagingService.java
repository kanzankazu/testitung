/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Utils;
import com.kanzankazu.itungitungan.view.main.Hutang.HutangListActivity;
import com.kanzankazu.itungitungan.view.main.MainActivity;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationModel notif;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> m = remoteMessage.getData();
            JSONObject mapToObject = new JSONObject(m);
            Log.d("Lihat", "onMessageReceived MyFirebaseMessagingService : " + mapToObject);

            notif = (NotificationModel) Utils.convertJsonToObjectClass(NotificationModel.class, mapToObject.toString());
        }

        makeNotification1(notif, remoteMessage);
    }

    private void makeNotification1(NotificationModel notif, RemoteMessage remoteMessage) {
        final Intent intent;
        if (notif.getType().equalsIgnoreCase(Constants.FirebasePushNotif.TypeNotif.hutang)) {
            intent = new Intent(this, HutangListActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        if (TextUtils.isEmpty(notif.getId())) intent.putExtra(Constants.Bundle.ID, notif.getId());
        if (TextUtils.isEmpty(notif.getIdSub())) intent.putExtra(Constants.Bundle.ID_SUB, notif.getIdSub());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wallet);

        String title = (!TextUtils.isEmpty(notif.getTitle())) ? notif.getTitle() : remoteMessage.getNotification().getTitle();
        String message = (!TextUtils.isEmpty(notif.getMessage())) ? notif.getMessage() : remoteMessage.getNotification().getBody();

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wallet)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void makeNotification2(String messageBody, Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Remote Router Notification")
                .setSmallIcon(R.drawable.ic_wallet)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_wallet))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setLights(Color.GREEN, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
