/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kanzankazu.itungitungan.util.Firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.view.main.Hutang.HutangListActivity
import com.kanzankazu.itungitungan.view.main.MainActivity
import org.json.JSONObject
import java.util.*

abstract class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"
    private var notif: NotificationModel? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val m = remoteMessage.data
            val mapToObject = JSONObject(m)
            Log.d("Lihat", "onMessageReceived MyFirebaseMessagingService : $mapToObject")

            notif = Utils.convertJsonToObjectClass(NotificationModel::class.java, mapToObject.toString()) as NotificationModel
        }

        makeNotification1(notif!!, remoteMessage)
    }

    private fun makeNotification1(notif: NotificationModel, remoteMessage: RemoteMessage) {
        val intent: Intent = when (notif.type) {
            Constants.FirebasePushNotif.TypeNotif.hutang -> {
                Intent(this, HutangListActivity::class.java)
            }
            else -> Intent(this, MainActivity::class.java)
        }

        if (notif.id.isNotEmpty()) intent.putExtra(Constants.Bundle.ID, notif.id)
        if (notif.idSub.isNotEmpty()) intent.putExtra(Constants.Bundle.ID_SUB, notif.id)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.title)
        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.body)
        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.icon)
        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.color)
        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.tag)
        Log.d("Lihat makeNotification1 MyFirebaseMessagingService", remoteMessage.notification?.link.toString())

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_wallet)

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wallet)
                .setLargeIcon(largeIcon)
                .setContentTitle(if (notif.title.isNotEmpty()) notif.title else remoteMessage.notification!!.title)
                .setContentText(if (notif.message.isNotEmpty()) notif.message else remoteMessage.notification!!.body)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        }
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }

    private fun makeNotification2(messageBody: String, targetClass: Class<*>) {
        val intent = Intent(this, targetClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle("Remote Router Notification")
                .setSmallIcon(R.drawable.ic_wallet)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_wallet))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setLights(Color.GREEN, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }
}
