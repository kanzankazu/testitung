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

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.UserPreference;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String SUBSCRIBE_TO = "userABC";

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Lihat", "onTokenRefresh MyFirebaseInstanceIDService : " + token);

        UserPreference.getInstance().setFCMToken(token);

        //FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);

        if (UserPreference.getInstance().isContainKey(Constants.SharedPreference.UID)) {
            sendRegistrationToServer(token);
        }
    }

    private void sendRegistrationToServer(String token) {
        String uid = UserPreference.getInstance().getUid();
        FirebaseDatabaseHandler.setFCMTokenUser(uid, token, new FirebaseDatabaseUtil.ValueListenerString() {
            @Override
            public void onSuccess(String message) {
                Log.d("Lihat", "onSuccess MyFirebaseInstanceIDService : " + message);
            }

            @Override
            public void onFailure(String message) {
                Log.d("Lihat", "onFailure MyFirebaseInstanceIDService : " + message);
            }
        });
    }
}
