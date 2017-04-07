package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.five.shubhamagarwal.five.R;

/**
 * Created by shubhamagrawal on 07/04/17.
 */

// expects notification message in certain format. http://stackoverflow.com/questions/37565599/firebase-cloud-messaging-fcm-launch-activity-when-user-clicks-the-notificati

public class NotificationActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startingIntent = getIntent();

        if (startingIntent != null) {
            final String hasSharedProfile = startingIntent.getStringExtra("has_shared_profile"); // Retrieve the id
            final String message  = startingIntent.getStringExtra("message");
            final String facebookLink = startingIntent.getStringExtra("facebook_link");


            Button action;
            if(hasSharedProfile.equals("true") || hasSharedProfile.equals("yes")){
                setContentView(R.layout.activity_congrats);
                TextView messageView = (TextView) findViewById(R.id.message);
                messageView.setText("''  " + message + "  ''");
                action = (Button) findViewById(R.id.notification_activity_action);
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(facebookLink);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            } else {
                setContentView(R.layout.activity_oops);
                action = (Button) findViewById(R.id.notification_activity_action);
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        System.exit(0);
                    }
                });
            }
        }
    }
}
