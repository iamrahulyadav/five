package com.spate.in;

import android.os.Bundle;

import com.spate.in.utils.Gen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by shubhamagrawal on 07/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    // https://firebase.google.com/docs/notifications/android/console-device#access_the_registration_token

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        Gen.handleNotification(bundle);
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
