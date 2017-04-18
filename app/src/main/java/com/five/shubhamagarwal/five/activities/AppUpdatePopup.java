package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.five.shubhamagarwal.five.BuildConfig;
import com.five.shubhamagarwal.five.R;

/**
 * Created by shubhamagrawal on 19/04/17.
 */

public class AppUpdatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_popup);
        final String appLink = BuildConfig.APP_LINK;

        Button action;
        action = (Button) findViewById(R.id.app_update_link_button);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(appLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}