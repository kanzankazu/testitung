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

package com.kanzankazu.itungitungan.view.util.Firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> m = remoteMessage.getData();
            List<String> list1 = new ArrayList<String>(m.keySet());
            List<String> list21 = new ArrayList<String>(m.values());
            JSONObject mapToObject = new JSONObject(m);
        }

        if (remoteMessage.getNotification() != null) {
        }

        makeNotification(remoteMessage.getNotification().getBody());
    }

    private void makeNotification(String messageBody) {
        /*Intent intent = new Intent(this, MainWebView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Remote Router Notification")
                .setSmallIcon(R.drawable.ic_notif_remoterouter)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_notif_remoterouter))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setLights(Color.GREEN, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/
    }
}
