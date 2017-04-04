package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import java.util.Calendar;
import java.util.Date;


public class CallStatusActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_status);
    }
    private static TextView mkeepCalm1, mkeepCalm2, mTimerView;

    public interface CallStatus{
        String NO_CALL = "Till we schedule your next call.";
        String CALL_IN = "Your next call in ...";
    }
    @Override
    protected void onStart() {
        super.onStart();

        final Button mCallButton = (Button) findViewById(R.id.call_button);
        mkeepCalm1 = (TextView) findViewById(R.id.keep_calm_text1);
        mkeepCalm2 = (TextView) findViewById(R.id.keep_calm_text2);
        mTimerView = (TextView) findViewById(R.id.timer_id);

        mkeepCalm2.setText(CallStatus.CALL_IN);

        mCallButton.setVisibility(View.INVISIBLE);
        mCallButton.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.SECOND, 3);
        final Date scheduledTime = calendar.getTime();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while ((scheduledTime.getTime() - Calendar.getInstance().getTime().getTime()) >= 0) {
                        final Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Date currentTime = Calendar.getInstance().getTime();
                                String time = Gen.getTimeDiffFromCurrentTime(scheduledTime);
                                mTimerView.setText(time);
                                if ((scheduledTime.getTime() - currentTime.getTime())/1000 <= 0){
                                    mCallButton.setVisibility(View.VISIBLE);

                                    mTimerView.setVisibility(View.INVISIBLE);
                                    mkeepCalm1.setVisibility(View.INVISIBLE);
                                    mkeepCalm2.setVisibility(View.INVISIBLE);
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
                Gen.toast("We are connecting u now.....", this);
                Intent intent =  new Intent(this, CallActivity.class);
                startActivity(intent);
            }
        }
    }
}
