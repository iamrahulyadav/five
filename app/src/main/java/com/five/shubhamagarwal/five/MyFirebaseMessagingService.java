package com.five.shubhamagarwal.five;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by shubhamagrawal on 07/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    // https://firebase.google.com/docs/notifications/android/console-device#access_the_registration_token

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
