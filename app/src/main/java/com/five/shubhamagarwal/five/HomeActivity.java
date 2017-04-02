package com.five.shubhamagarwal.five;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InterruptedIOException;
import java.util.Calendar;
import java.util.Date;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



    }

    @Override
    protected void onStart() {
        super.onStart();

        final Button mCallButton = (Button) findViewById(R.id.call_button);
        mCallButton.setVisibility(View.GONE);
        mCallButton.setOnClickListener(this);
        final TextView mTimerView = (TextView) findViewById(R.id.timerId);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.SECOND, 3);
        final Date scheduledTime = calendar.getTime();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    final Boolean timerStart = true;
                    while ((scheduledTime.getTime() - Calendar.getInstance().getTime().getTime()) >= 0) {
                        final Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Date currentTime = Calendar.getInstance().getTime();
                                long seconds = (scheduledTime.getTime() - currentTime.getTime())/1000;
                                long hour = seconds/3600;
                                seconds = seconds%3600;
                                long min = seconds/60;
                                seconds = seconds%60;
                                String time = String.format("%02d:%02d:%02d", hour, min, seconds);
                                mTimerView.setText(time);
                                if ((scheduledTime.getTime() - currentTime.getTime())/1000 <= 0){
                                    mTimerView.setVisibility(View.INVISIBLE);
                                    mCallButton.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        runOnUiThread(tr);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_button: {
                Toast.makeText(this, "We are connecting u now.....", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(this, CallActivity.class);
                startActivity(intent);
            }
        }
    }
}
